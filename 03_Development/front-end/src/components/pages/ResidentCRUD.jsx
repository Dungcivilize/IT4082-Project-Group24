import React, { useEffect, useState } from 'react';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/residents';

const ResidentCRUD = () => {
  const [residents, setResidents] = useState([]);
  const [selectedResidentId, setSelectedResidentId] = useState('');
  const [citizens, setCitizens] = useState([]);
  const [address, setAddress] = useState('');
  const [loadingCitizens, setLoadingCitizens] = useState(false);
  const [newCitizen, setNewCitizen] = useState({ fullname: '', phone: '' });
  const [addingCitizen, setAddingCitizen] = useState(false);

  // Load danh sách hộ gia đình
  useEffect(() => {
    axios.get(API_URL)
      .then(res => setResidents(res.data))
      .catch(err => console.error('Lỗi khi lấy danh sách hộ:', err));
  }, []);

  // Load citizens khi chọn resident
  const loadCitizens = (id) => {
    setLoadingCitizens(true);
    axios.get(`${API_URL}/${id}/citizens`)
      .then(res => setCitizens(res.data))
      .catch(err => {
        console.error('Lỗi khi lấy danh sách công dân:', err);
        setCitizens([]);
      })
      .finally(() => setLoadingCitizens(false));
  };

  const handleResidentChange = (e) => {
    const id = e.target.value;
    setSelectedResidentId(id);
    setCitizens([]);
    setAddress('');
    setNewCitizen({ fullname: '', phone: '' });

    if (!id) return;

    const resident = residents.find(r => r.residentId.toString() === id);
    setAddress(resident?.address || '');

    loadCitizens(id);
  };

  // Xóa công dân
  const handleDeleteCitizen = (citizenId) => {
    if (!window.confirm('Bạn có chắc muốn xóa thành viên này?')) return;

    axios.delete(`http://localhost:8080/api/citizens/${citizenId}`)
      .then(() => {
        setCitizens(prev => prev.filter(c => c.citizenId !== citizenId));
      })
      .catch(err => alert('Xóa thất bại: ' + err.message));
  };

  // Thêm công dân mới
  const handleAddCitizen = (e) => {
    e.preventDefault();
    if (!newCitizen.fullname.trim() || !newCitizen.phone.trim()) {
      alert('Vui lòng điền đầy đủ họ tên và số điện thoại');
      return;
    }

    setAddingCitizen(true);

    axios.post(`http://localhost:8080/api/residents/${selectedResidentId}/citizens`, newCitizen)
      .then(res => {
        setCitizens(prev => [...prev, res.data]);
        setNewCitizen({ fullname: '', phone: '' });
      })
      .catch(err => alert('Thêm thất bại: ' + err.message))
      .finally(() => setAddingCitizen(false));
  };

  return (
    <div style={{ maxWidth: 900, margin: 'auto', padding: 20, fontFamily: 'Arial, sans-serif' }}>
      <h2 style={{ textAlign: 'center', marginBottom: 30 }}>Quản lý hộ gia đình</h2>

      <div style={{ marginBottom: 20 }}>
        <label style={{ fontWeight: 'bold', marginRight: 10 }}>Chọn hộ gia đình:</label>
        <select
          value={selectedResidentId}
          onChange={handleResidentChange}
          style={{
            padding: '8px 12px',
            borderRadius: 5,
            border: '1px solid #ccc',
            minWidth: 200,
          }}
        >
          <option value="">-- Chọn hộ gia đình --</option>
          {residents.map(resident => (
            <option key={resident.residentId} value={resident.residentId}>
              {resident.householderId || resident.residentId}
            </option>
          ))}
        </select>
      </div>

      {selectedResidentId && (
        <>
          <h3>Địa chỉ hộ:</h3>
          <p style={{ marginBottom: 20, fontStyle: 'italic', color: '#555' }}>{address || 'Chưa có địa chỉ'}</p>

          <h3>Danh sách thành viên trong hộ</h3>
          {loadingCitizens ? (
            <p>Đang tải thành viên...</p>
          ) : citizens.length === 0 ? (
            <p>Chưa có thành viên nào</p>
          ) : (
            <table
              style={{
                width: '100%',
                borderCollapse: 'collapse',
                marginBottom: 20,
              }}
            >
              <thead>
                <tr style={{ backgroundColor: '#007BFF', color: 'white' }}>
                  <th style={{ padding: 10, border: '1px solid #ddd' }}>ID</th>
                  <th style={{ padding: 10, border: '1px solid #ddd' }}>Họ tên</th>
                  <th style={{ padding: 10, border: '1px solid #ddd' }}>Số điện thoại</th>
                  <th style={{ padding: 10, border: '1px solid #ddd' }}>Hành động</th>
                </tr>
              </thead>
              <tbody>
                {citizens.map(citizen => (
                  <tr key={citizen.citizenId} style={{ textAlign: 'center', cursor: 'default' }}>
                    <td style={{ padding: 8, border: '1px solid #ddd' }}>{citizen.citizenId}</td>
                    <td style={{ padding: 8, border: '1px solid #ddd' }}>{citizen.fullname}</td>
                    <td style={{ padding: 8, border: '1px solid #ddd' }}>{citizen.phone}</td>
                    <td style={{ padding: 8, border: '1px solid #ddd' }}>
                      <button
                        onClick={() => handleDeleteCitizen(citizen.citizenId)}
                        style={{
                          backgroundColor: '#dc3545',
                          border: 'none',
                          color: 'white',
                          padding: '6px 12px',
                          borderRadius: 4,
                          cursor: 'pointer',
                        }}
                      >
                        Xóa
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </>
      )}
    </div>
  );
};

export default ResidentCRUD;
