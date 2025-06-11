import React, { useState, useEffect } from 'react';
import { Home, Plus, Users, Search, X, Check } from 'lucide-react';
import './ApartmentEmpty.css';

const ApartmentEmpty = () => {
  const [emptyApartments, setEmptyApartments] = useState([]);
  const [availableUsers, setAvailableUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showAddHouseholdModal, setShowAddHouseholdModal] = useState(false);
  const [selectedApartment, setSelectedApartment] = useState(null);
  const [selectedUser, setSelectedUser] = useState(null);
  const [userSearchTerm, setUserSearchTerm] = useState('');
  const [creating, setCreating] = useState(false);

  useEffect(() => {
    fetchEmptyApartments();
    fetchAvailableUsers();
  }, []);

  const fetchEmptyApartments = async () => {
    try {
      setLoading(true);
      const response = await fetch('http://localhost:8080/api/admin/apartments/empty');
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const data = await response.json();
      console.log('Empty apartments:', data);
      setEmptyApartments(data);
    } catch (error) {
      console.error('Error fetching empty apartments:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchAvailableUsers = async () => {
    try {
      // Assuming you have an API to get users who don't currently own any apartment
      const response = await fetch('http://localhost:8080/api/users/no-active-ownership');
      
      if (response.ok) {
        const data = await response.json();
        setAvailableUsers(data);
      }
    } catch (error) {
      console.error('Error fetching available users:', error);
    }
  };

  const handleAddHousehold = (apartment) => {
    setSelectedApartment(apartment);
    setSelectedUser(null);
    setUserSearchTerm('');
    setShowAddHouseholdModal(true);
  };

  const handleCloseModal = () => {
    setShowAddHouseholdModal(false);
    setSelectedApartment(null);
    setSelectedUser(null);
    setUserSearchTerm('');
  };

  const handleUserSelect = (user) => {
    setSelectedUser(user);
    setUserSearchTerm(user.fullName);
  };

  const handleCreateOwnership = async () => {
    if (!selectedApartment || !selectedUser) {
      alert('Vui lòng chọn đầy đủ thông tin');
      return;
    }

    try {
      setCreating(true);

      // Create new apartment ownership
      const ownershipData = {
        apartmentId: selectedApartment.apartmentId,
        userId: selectedUser.userId,
        startDate: new Date().toISOString().split('T')[0], // Today's date
        status: 'active'
      };

      const ownershipResponse = await fetch('http://localhost:8080/api/ownerships', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(ownershipData)
      });

      if (!ownershipResponse.ok) {
        throw new Error('Failed to create ownership');
      }

      // Update apartment status to occupied
      const apartmentUpdateResponse = await fetch(
        `http://localhost:8080/api/admin/apartments/${selectedApartment.apartmentId}`,
        {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            ...selectedApartment,
            status: 'occupied'
          })
        }
      );

      if (!apartmentUpdateResponse.ok) {
        throw new Error('Failed to update apartment status');
      }

      // Refresh the empty apartments list
      await fetchEmptyApartments();
      
      // Close modal
      handleCloseModal();
      
      alert('Tạo hộ gia đình thành công!');
      
    } catch (error) {
      console.error('Error creating ownership:', error);
      alert('Có lỗi xảy ra khi tạo hộ gia đình');
    } finally {
      setCreating(false);
    }
  };

  const filteredUsers = availableUsers.filter(user =>
    user.fullName.toLowerCase().includes(userSearchTerm.toLowerCase()) ||
    user.email.toLowerCase().includes(userSearchTerm.toLowerCase()) ||
    user.phone.includes(userSearchTerm)
  );

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Đang tải dữ liệu...</p>
      </div>
    );
  }

  return (
    <div className="apartment-empty">
      <div className="header">
        <h1>
          <Home size={28} />
          Quản lý căn hộ trống
        </h1>
        <p className="subtitle">
          Danh sách các căn hộ đang trống và sẵn sàng cho thuê
        </p>
      </div>

      <div className="content-container">
        {emptyApartments.length > 0 ? (
          <div className="apartments-grid">
            {emptyApartments.map((apartment) => (
              <div key={apartment.apartmentId} className="apartment-card">
                <div className="apartment-header">
                  <h3 className="apartment-title">
                    <Home size={20} />
                    Căn hộ {apartment.apartmentCode}
                  </h3>
                  <span className="status-badge status-empty">Trống</span>
                </div>
                
                <div className="apartment-info">
                  <div className="info-item">
                    <span className="info-label">Tầng:</span>
                    <span className="info-value">{apartment.floor}</span>
                  </div>
                  <div className="info-item">
                    <span className="info-label">Diện tích:</span>
                    <span className="info-value">{apartment.area} m²</span>
                  </div>
                </div>

                <button 
                  className="add-household-btn"
                  onClick={() => handleAddHousehold(apartment)}
                >
                  <Plus size={16} />
                  Thêm hộ gia đình
                </button>
              </div>
            ))}
          </div>
        ) : (
          <div className="empty-state">
            <Home className="empty-icon" />
            <h3>Không có căn hộ trống</h3>
            <p>Tất cả căn hộ đã được sử dụng</p>
          </div>
        )}
      </div>

      {/* Add Household Modal */}
      {showAddHouseholdModal && (
        <div className="modal-overlay">
          <div className="add-household-modal">
            <div className="modal-header">
              <h3>
                <Users size={20} />
                Thêm hộ gia đình - Căn hộ {selectedApartment?.apartmentCode}
              </h3>
              <button className="close-btn" onClick={handleCloseModal}>
                <X size={20} />
              </button>
            </div>

            <div className="modal-content">
              <div className="apartment-info-section">
                <h4>Thông tin căn hộ</h4>
                <div className="apartment-details">
                  <div className="detail-item">
                    <span>Mã căn hộ:</span>
                    <span>{selectedApartment?.apartmentCode}</span>
                  </div>
                  <div className="detail-item">
                    <span>Tầng:</span>
                    <span>{selectedApartment?.floor}</span>
                  </div>
                  <div className="detail-item">
                    <span>Diện tích:</span>
                    <span>{selectedApartment?.area} m²</span>
                  </div>
                </div>
              </div>

              <div className="user-selection-section">
                <h4>Chọn chủ hộ</h4>
                <div className="user-search">
                  <div className="search-input-container">
                    <Search size={16} className="search-icon" />
                    <input
                      type="text"
                      placeholder="Tìm kiếm người dùng (tên, email, số điện thoại)..."
                      value={userSearchTerm}
                      onChange={(e) => setUserSearchTerm(e.target.value)}
                      className="search-input"
                    />
                  </div>
                </div>

                <div className="users-list">
                  {filteredUsers.length > 0 ? (
                    filteredUsers.map((user) => (
                      <div
                        key={user.userId}
                        className={`user-item ${selectedUser?.userId === user.userId ? 'selected' : ''}`}
                        onClick={() => handleUserSelect(user)}
                      >
                        <div className="user-info">
                          <div className="user-name">{user.fullName}</div>
                          <div className="user-details">
                            <span>{user.email}</span>
                            <span>•</span>
                            <span>{user.phone}</span>
                          </div>
                        </div>
                        {selectedUser?.userId === user.userId && (
                          <Check size={16} className="check-icon" />
                        )}
                      </div>
                    ))
                  ) : (
                    <div className="no-users">
                      Không tìm thấy người dùng phù hợp
                    </div>
                  )}
                </div>
              </div>
            </div>

            <div className="modal-actions">
              <button className="cancel-btn" onClick={handleCloseModal}>
                Hủy
              </button>
              <button 
                className="create-btn" 
                onClick={handleCreateOwnership}
                disabled={!selectedUser || creating}
              >
                {creating ? 'Đang tạo...' : 'Tạo hộ gia đình'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ApartmentEmpty;