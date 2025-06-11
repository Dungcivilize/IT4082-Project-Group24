import React, { useState, useEffect, useRef } from 'react';
import { Users, Car, Home, ChevronRight, Eye, MoreVertical, LogOut, CalendarCheck, CalendarX } from 'lucide-react';
import './ManageHousehold.css';

const ManageHousehold = () => {
  const [households, setHouseholds] = useState([]);
  const [selectedHousehold, setSelectedHousehold] = useState(null);
  const [residents, setResidents] = useState([]);
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('residents');
  const [dropdownOpen, setDropdownOpen] = useState(null);
  const [confirmMoveOut, setConfirmMoveOut] = useState(null);
  const dropdownRef = useRef(null);
  const [temporaryResidents, setTemporaryResidents] = useState([]);
  const [temporaryAbsents, setTemporaryAbsents] = useState([]);

  // Fetch all active households
  useEffect(() => {
    fetchHouseholds();
  }, []);

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setDropdownOpen(null);
      }
    };

    document.addEventListener('click', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const fetchHouseholds = async () => {
    try {
      setLoading(true);
      const response = await fetch('http://localhost:8080/api/ownerships/active');
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const data = await response.json();
      
      // Debug logging
      console.log('Households response:', data);
      if (data.length > 0) {
        console.log('Sample household object:', data[0]);
      }
      
      setHouseholds(data);
    } catch (error) {
      console.error('Error fetching households:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchResidents = async (ownershipId) => {
    if (!ownershipId || ownershipId === 'undefined') {
      console.error('Invalid ownership ID for residents:', ownershipId);
      setResidents([]);
      return;
    }
    
    try {
      console.log('Fetching residents for ownership ID:', ownershipId);
      const response = await fetch(`http://localhost:8080/api/ownerships/${ownershipId}/residents`);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const data = await response.json();
      setResidents(data);
    } catch (error) {
      console.error('Error fetching residents:', error);
      setResidents([]);
    }
  };

  const fetchVehicles = async (ownershipId) => {
    if (!ownershipId || ownershipId === 'undefined') {
      console.error('Invalid ownership ID for vehicles:', ownershipId);
      setVehicles([]);
      return;
    }
    
    try {
      console.log('Fetching vehicles for ownership ID:', ownershipId);
      const response = await fetch(`http://localhost:8080/api/ownerships/${ownershipId}/vehicles`);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const data = await response.json();
      setVehicles(data);
    } catch (error) {
      console.error('Error fetching vehicles:', error);
      setVehicles([]);
    }
  };

    const fetchTemporaryResidents = async (ownershipId) => {
  if (!ownershipId) return;
  try {
    const res = await fetch(`http://localhost:8080/api/ownerships/${ownershipId}/temporary-residents`);
    if (!res.ok) throw new Error('Lỗi khi fetch tạm trú');
    const data = await res.json();
    setTemporaryResidents(data);
  } catch (error) {
    console.error('Lỗi fetch temporary residents:', error);
    setTemporaryResidents([]);
  }
};

const fetchTemporaryAbsents = async (ownershipId) => {
  if (!ownershipId) return;
  try {
    const res = await fetch(`http://localhost:8080/api/ownerships/${ownershipId}/temporary-absents`);
    if (!res.ok) throw new Error('Lỗi khi fetch tạm vắng');
    const data = await res.json();
    setTemporaryAbsents(data);
  } catch (error) {
    console.error('Lỗi fetch temporary absents:', error);
    setTemporaryAbsents([]);
  }
};


  const handleHouseholdSelect = async (household) => {
    if (!household.ownershipId) {
      console.error('No ownershipId found in household object:', household);
      return;
    }
    
    console.log('Selected household:', household);
    console.log('Using ownership ID:', household.ownershipId);
    
    setSelectedHousehold(household);
    setActiveTab('residents');
    
    try {
      await Promise.all([
        fetchResidents(household.ownershipId),
        fetchVehicles(household.ownershipId),
        fetchTemporaryResidents(household.ownershipId),
        fetchTemporaryAbsents(household.ownershipId)
      ]);
    } catch (error) {
      console.error('Error fetching household details:', error);
    }
  };

  const handleBackToList = () => {
    setSelectedHousehold(null);
    setResidents([]);
    setVehicles([]);
  };

  const handleDropdownToggle = (e, householdId) => {
    e.stopPropagation(); // Prevent card click
    setDropdownOpen(dropdownOpen === householdId ? null : householdId);
  };

  const handleMoveOutClick = (e, household) => {
    e.stopPropagation();
    setDropdownOpen(null);
    setConfirmMoveOut(household);
  };

  const handleConfirmMoveOut = async () => {
  if (!confirmMoveOut) return;

  try {
    // 1. Cập nhật ownership thành inactive
    const ownershipRes = await fetch(`http://localhost:8080/api/ownerships/${confirmMoveOut.ownershipId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        ...confirmMoveOut,
        ownershipStatus: 'inactive'
      })
    });
    if (!ownershipRes.ok) throw new Error('Failed to update ownership status');

    // 3. Load lại danh sách hộ và danh sách phòng
    await fetchHouseholds();      // refresh ownerships or residents

    // 4. Đóng dialog xác nhận
    setConfirmMoveOut(null);

    // 5. Thông báo thành công (có thể thêm toast/snackbar)
    console.log('Household moved out successfully');

  } catch (error) {
    console.error('Error moving out household:', error);
  }
};

  const handleCancelMoveOut = () => {
    setConfirmMoveOut(null);
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Đang tải dữ liệu...</p>
      </div>
    );
  }

  return (
    <div className="manage-household">
      <div className="header">
        <h1>
          <Home size={28} />
          Quản lý hộ gia đình
        </h1>
        
        <div className="breadcrumb">
          <span className="breadcrumb-item" onClick={handleBackToList}>
            Danh sách hộ gia đình
          </span>
          {selectedHousehold && (
            <>
              <ChevronRight size={16} className="breadcrumb-separator" />
              <span>Chi tiết hộ {selectedHousehold.apartmentCode}</span>
            </>
          )}
        </div>
      </div>

      <div className="content-container">
        {!selectedHousehold ? (
          // Household List View
          <div className="household-grid">
            {households.map((household) => (
              <div
                key={household.ownershipId}
                className="household-card"
                onClick={() => handleHouseholdSelect(household)}
              >
                <div className="household-header">
                  <h3 className="household-title">
                    <Home size={20} />
                    Căn hộ {household.apartmentCode}
                  </h3>
                  
                  {/* Three dots menu */}
                  <div className="dropdown-container" ref={dropdownRef}>
                    <button
                      className="dropdown-trigger"
                      onClick={(e) => handleDropdownToggle(e, household.ownershipId)}
                    >
                      <MoreVertical size={16} />
                    </button>
                    
                    {dropdownOpen === household.ownershipId && (
                      <div className="dropdown-menu">
                        <button
                          className="dropdown-item move-out"
                          onClick={(e) => handleMoveOutClick(e, household)}
                        >
                          <LogOut size={14} />
                          Dời đi
                        </button>
                      </div>
                    )}
                  </div>
                </div>
                
                <div className="household-info">
                  <div className="info-item">
                    <Users size={16} />
                    <span>Chủ hộ: <span className="info-value">{household.ownershipName}</span></span>
                  </div>
                  <div className="info-item">
                    <span>Phòng: <span className="info-value">{household.apartmentCode}</span></span>
                  </div>
                  <div className="info-item">
                    <span>Tầng: <span className="info-value">{household.floor}</span></span>
                  </div>
                  <div className="info-item">
                    <span
                      className={`status-badge ${
                        household.ownershipStatus?.toLowerCase() === 'active'
                          ? 'status-active'
                          : 'status-inactive'
                      }`}
                    >
                      {household.ownershipStatus?.toLowerCase() === 'active'
                        ? 'Đang sử dụng'
                        : 'Không sử dụng'}
                    </span>
                  </div>
                </div>

                <div className="view-details">
                  <Eye size={16} />
                  Xem chi tiết
                </div>
              </div>
            ))}
          </div>
        ) : (
          // Detail View
          <div className="detail-view">
            <div className="detail-header">
              <h2 className="detail-title">
                <Home size={24} />
                Chi tiết căn hộ {selectedHousehold.apartmentCode}
              </h2>
              <button className="back-button" onClick={handleBackToList}>
                ← Quay lại danh sách
              </button>
            </div>

            <div className="tabs">
              <div
                className={`tab ${activeTab === 'residents' ? 'active' : ''}`}
                onClick={() => setActiveTab('residents')}
              >
                <Users size={18} />
                Thành viên ({residents.length})
              </div>
              <div
                className={`tab ${activeTab === 'vehicles' ? 'active' : ''}`}
                onClick={() => setActiveTab('vehicles')}
              >
                <Car size={18} />
                Phương tiện ({vehicles.length})
              </div>
              <div
                className={`tab ${activeTab === 'temporaryResidence' ? 'active' : ''}`}
                onClick={() => setActiveTab('temporaryResidence')}
              >
                <CalendarCheck size={18} />
                Tạm trú ({temporaryResidents.length})
              </div>
              <div
                className={`tab ${activeTab === 'temporaryAbsence' ? 'active' : ''}`}
                onClick={() => setActiveTab('temporaryAbsence')}
              >
                <CalendarX size={18} />
                Tạm vắng ({temporaryAbsents.length})
              </div>
            </div>

            <div className="tab-content">
              {activeTab === 'residents' && (
                <>
                  {residents.length > 0 ? (
                    residents.map((resident) => (
                      <div key={resident.residentId} className="resident-card">
                        <h4 className="card-title">
                          <Users size={18} />
                          {resident.fullName}
                        </h4>
                        <div className="card-info">
                          <div className="card-field">
                            <span className="field-label">CCCD:</span>
                            <span className="field-value">{resident.identityCard}</span>
                          </div>
                          <div className="card-field">
                            <span className="field-label">Ngày sinh:</span>
                            <span className="field-value">{resident.birthDate}</span>
                          </div>
                          <div className="card-field">
                            <span className="field-label">SĐT:</span>
                            <span className="field-value">{resident.phone}</span>
                          </div>
                          <div className="card-field">
                            <span className="field-label">Quan hệ:</span>
                            <span className="field-value">{resident.relationship || 'Chủ hộ'}</span>
                          </div>
                        </div>
                      </div>
                    ))
                  ) : (
                    <div className="empty-state">
                      <Users className="empty-icon" />
                      <p>Không có thành viên nào trong hộ này</p>
                    </div>
                  )}
                </>
              )}

              {activeTab === 'vehicles' && (
                <>
                  {vehicles.length > 0 ? (
                    vehicles.map((vehicle) => (
                      <div key={vehicle.vehicleId} className="vehicle-card">
                        <h4 className="card-title">
                          <Car size={18} />
                          {vehicle.licensePlate}
                        </h4>
                        <div className="card-info">
                          <div className="card-field">
                            <span className="field-label">Loại xe:</span>
                            <span className="field-value">{vehicle.type}</span>
                          </div>
                          <div className="card-field">
                            <span className="field-label">Tên chủ sở hữu:</span>
                            <span className="field-value">{vehicle.vehicleOwnerName}</span>
                          </div>
                        </div>
                      </div>
                    ))
                  ) : (
                    <div className="empty-state">
                      <Car className="empty-icon" />
                      <p>Không có phương tiện nào trong hộ này</p>
                    </div>
                  )}
                </>
              )}

              {activeTab === 'temporaryResidence' && (
  <>
    {temporaryResidents.length > 0 ? (
      temporaryResidents.map((entry) => (
        <div key={entry.temporaryResidentId} className="temp-card">
          <h4 className="card-title">
            <CalendarCheck size={18} />
            {entry.fullName}
          </h4>
          <div className="card-info">
            <div className="card-field">
              <span className="field-label">Ngày sinh:</span>
              <span className="field-value">{entry.birthDate}</span>
            </div>
            <div className="card-field">
              <span className="field-label">Giới tính:</span>
              <span className="field-value">{entry.gender}</span>
            </div>
            <div className="card-field">
              <span className="field-label">CCCD:</span>
              <span className="field-value">{entry.identityCard}</span>
            </div>
            <div className="card-field">
              <span className="field-label">SĐT:</span>
              <span className="field-value">{entry.phone}</span>
            </div>
            <div className="card-field">
              <span className="field-label">Từ ngày:</span>
              <span className="field-value">{entry.startDate}</span>
            </div>
            <div className="card-field">
              <span className="field-label">Đến ngày:</span>
              <span className="field-value">{entry.endDate}</span>
            </div>
            <div className="card-field">
              <span className="field-label">Lý do:</span>
              <span className="field-value">{entry.reason}</span>
            </div>
          </div>
        </div>
      ))
    ) : (
      <div className="empty-state">
        <CalendarCheck className="empty-icon" />
        <p>Không có người tạm trú nào</p>
      </div>
    )}
  </>
)}

{activeTab === 'temporaryAbsence' && (
  <>
    {temporaryAbsents.length > 0 ? (
      temporaryAbsents.map((entry) => (
        <div key={entry.temporaryAbsentId} className="temp-card">
          <h4 className="card-title">
            <CalendarX size={18} />
            {entry.fullName}
          </h4>
          <div className="card-info">
            <div className="card-field">
              <span className="field-label">Từ ngày:</span>
              <span className="field-value">{entry.startDate}</span>
            </div>
            <div className="card-field">
              <span className="field-label">Đến ngày:</span>
              <span className="field-value">{entry.endDate}</span>
            </div>
            <div className="card-field">
              <span className="field-label">Địa chỉ tạm vắng:</span>
              <span className="field-value">{entry.temporaryAddress}</span>
            </div>
            <div className="card-field">
              <span className="field-label">Lý do:</span>
              <span className="field-value">{entry.reason}</span>
            </div>
          </div>
        </div>
      ))
    ) : (
      <div className="empty-state">
        <CalendarX className="empty-icon" />
        <p>Không có người tạm vắng nào</p>
      </div>
    )}
  </>
)}


            </div>
          </div>
        )}
      </div>

      {/* Confirmation Modal */}
      {confirmMoveOut && (
        <div className="modal-overlay">
          <div className="confirmation-modal">
            <h3>Xác nhận dời đi</h3>
            <p>
              Bạn có chắc chắn muốn thực hiện dời đi cho căn hộ{' '}
              <strong>{confirmMoveOut.apartmentCode}</strong>?
            </p>
            <p className="warning-text">
              Hành động này sẽ:
              <br />
              • Đặt trạng thái hộ gia đình thành "Không hoạt động"
              <br />
              • Đặt trạng thái căn hộ thành "Trống"
            </p>
            <div className="modal-actions">
              <button className="cancel-btn" onClick={handleCancelMoveOut}>
                Hủy
              </button>
              <button className="confirm-btn" onClick={handleConfirmMoveOut}>
                Xác nhận dời đi
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ManageHousehold;