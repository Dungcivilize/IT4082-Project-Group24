import React, { useEffect, useState } from 'react';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/service';

const emptyService = {
  serviceName: '',
  description: '',
  fee: '',
};

const ServiceCRUD = () => {
  const [services, setServices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Form state
  const [form, setForm] = useState(emptyService);
  const [editingId, setEditingId] = useState(null);
  const [formError, setFormError] = useState('');

  // Load dịch vụ
  const fetchServices = () => {
    setLoading(true);
    axios.get(API_URL)
      .then(res => {
        setServices(res.data);
        setLoading(false);
      })
      .catch(() => {
        setError('Lấy dữ liệu dịch vụ thất bại');
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchServices();
  }, []);

  // Xử lý form input
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  // Thêm mới hoặc cập nhật
  const handleSubmit = (e) => {
    e.preventDefault();
    setFormError('');

    // Validate đơn giản
    if (!form.serviceName.trim()) {
      setFormError('Tên dịch vụ không được để trống');
      return;
    }
    if (!form.fee || isNaN(form.fee) || Number(form.fee) < 0) {
      setFormError('Phí phải là số lớn hơn hoặc bằng 0');
      return;
    }

    const data = {
      serviceName: form.serviceName.trim(),
      description: form.description.trim(),
      fee: Number(form.fee),
    };

    if (editingId === null) {
      // Thêm mới
      axios.post(API_URL, data)
        .then(() => {
          fetchServices();
          setForm(emptyService);
        })
        .catch(() => setFormError('Thêm dịch vụ thất bại'));
    } else {
      // Cập nhật
      axios.put(`${API_URL}/${editingId}`, data)
        .then(() => {
          fetchServices();
          setForm(emptyService);
          setEditingId(null);
        })
        .catch(() => setFormError('Cập nhật dịch vụ thất bại'));
    }
  };

  // Xóa dịch vụ
  const handleDelete = (id) => {
    if (!window.confirm('Bạn có chắc muốn xóa dịch vụ này không?')) return;

    axios.delete(`${API_URL}/${id}`)
      .then(() => fetchServices())
      .catch(() => alert('Xóa dịch vụ thất bại'));
  };

  // Bắt đầu sửa
  const handleEdit = (service) => {
    setForm({
      serviceName: service.serviceName,
      description: service.description,
      fee: service.fee.toString(),
    });
    setEditingId(service.serviceId);
    setFormError('');
  };

  // Hủy sửa
  const handleCancelEdit = () => {
    setForm(emptyService);
    setEditingId(null);
    setFormError('');
  };

  if (loading) return <p style={{textAlign: 'center', marginTop: 20}}>Đang tải dữ liệu dịch vụ...</p>;
  if (error) return <p style={{color: 'red', textAlign: 'center', marginTop: 20}}>{error}</p>;

  return (
    <div style={{
      maxWidth: 900,
      margin: '40px auto',
      padding: 20,
      fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
      backgroundColor: '#fff',
      boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
      borderRadius: 8
    }}>
      <h2 style={{textAlign: 'center', marginBottom: 30, color: '#333'}}>Quản lý dịch vụ</h2>

      <form onSubmit={handleSubmit} style={{
        marginBottom: 30,
        backgroundColor: '#f7f7f7',
        padding: 20,
        borderRadius: 6,
        boxShadow: 'inset 0 0 5px #ddd',
      }}>
        <h3 style={{marginBottom: 15}}>{editingId ? 'Chỉnh sửa dịch vụ' : 'Thêm dịch vụ mới'}</h3>

        <div style={{marginBottom: 12}}>
          <label style={{display: 'block', marginBottom: 6}}>Tên dịch vụ:</label>
          <input
            type="text"
            name="serviceName"
            value={form.serviceName}
            onChange={handleInputChange}
            style={{width: '100%', padding: 8, borderRadius: 4, border: '1px solid #ccc'}}
          />
        </div>

        <div style={{marginBottom: 12}}>
          <label style={{display: 'block', marginBottom: 6}}>Mô tả:</label>
          <textarea
            name="description"
            value={form.description}
            onChange={handleInputChange}
            rows={3}
            style={{width: '100%', padding: 8, borderRadius: 4, border: '1px solid #ccc'}}
          />
        </div>

        <div style={{marginBottom: 12}}>
          <label style={{display: 'block', marginBottom: 6}}>Phí (VNĐ):</label>
          <input
            type="number"
            name="fee"
            value={form.fee}
            onChange={handleInputChange}
            min="0"
            style={{width: '100%', padding: 8, borderRadius: 4, border: '1px solid #ccc'}}
          />
        </div>

        {formError && <p style={{color: 'red', marginBottom: 12}}>{formError}</p>}

        <div>
          <button type="submit" style={{
            padding: '10px 20px',
            backgroundColor: '#4CAF50',
            color: 'white',
            border: 'none',
            borderRadius: 4,
            cursor: 'pointer',
            marginRight: 10
          }}>
            {editingId ? 'Cập nhật' : 'Thêm'}
          </button>
          {editingId && (
            <button type="button" onClick={handleCancelEdit} style={{
              padding: '10px 20px',
              backgroundColor: '#f44336',
              color: 'white',
              border: 'none',
              borderRadius: 4,
              cursor: 'pointer'
            }}>
              Hủy
            </button>
          )}
        </div>
      </form>

      {services.length === 0 ? (
        <p style={{textAlign: 'center', color: '#666'}}>Chưa có dịch vụ nào.</p>
      ) : (
        <table style={{
          width: '100%',
          borderCollapse: 'separate',
          borderSpacing: 0,
          borderRadius: 8,
          overflow: 'hidden',
          boxShadow: '0 0 5px rgba(0,0,0,0.1)'
        }}>
          <thead>
            <tr style={{backgroundColor: '#4CAF50', color: 'white', textAlign: 'left'}}>
              <th style={{padding: '12px 15px'}}>ID</th>
              <th style={{padding: '12px 15px'}}>Tên dịch vụ</th>
              <th style={{padding: '12px 15px'}}>Mô tả</th>
              <th style={{padding: '12px 15px'}}>Phí (VNĐ)</th>
              <th style={{padding: '12px 15px', textAlign: 'center'}}>Hành động</th>
            </tr>
          </thead>
          <tbody>
            {services.map((service, idx) => (
              <tr
                key={service.serviceId}
                style={{
                  backgroundColor: idx % 2 === 0 ? '#f9f9f9' : '#fff',
                  transition: 'background-color 0.3s',
                  cursor: 'default'
                }}
                onMouseEnter={e => e.currentTarget.style.backgroundColor = '#e8f0fe'}
                onMouseLeave={e => e.currentTarget.style.backgroundColor = idx % 2 === 0 ? '#f9f9f9' : '#fff'}
              >
                <td style={{padding: '12px 15px'}}>{service.serviceId}</td>
                <td style={{padding: '12px 15px'}}>{service.serviceName}</td>
                <td style={{padding: '12px 15px'}}>{service.description}</td>
                <td style={{padding: '12px 15px', fontWeight: 'bold'}}>
                  {service.fee.toLocaleString('vi-VN')} ₫
                </td>
                <td style={{padding: '12px 15px', textAlign: 'center'}}>
                  <button
                    onClick={() => handleEdit(service)}
                    style={{
                      marginRight: 10,
                      backgroundColor: '#2196F3',
                      border: 'none',
                      color: 'white',
                      padding: '6px 12px',
                      borderRadius: 4,
                      cursor: 'pointer'
                    }}
                  >
                    Sửa
                  </button>
                  <button
                    onClick={() => handleDelete(service.serviceId)}
                    style={{
                      backgroundColor: '#f44336',
                      border: 'none',
                      color: 'white',
                      padding: '6px 12px',
                      borderRadius: 4,
                      cursor: 'pointer'
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
    </div>
  );
};

export default ServiceCRUD;
