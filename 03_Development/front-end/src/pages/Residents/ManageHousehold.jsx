import React, { useState, useEffect } from 'react';
import { Users, Car, Home, ChevronRight, Eye } from 'lucide-react';
import './ManageHousehold.css';

const ManageHousehold = () => {
  const [households, setHouseholds] = useState([]);
  const [selectedHousehold, setSelectedHousehold] = useState(null);
  const [residents, setResidents] = useState([]);
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('residents');

  // Fetch all active households
  useEffect(() => {
    fetchHouseholds();
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
        fetchVehicles(household.ownershipId)
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
                            <span className="field-label">ID chủ sở hữu:</span>
                            <span className="field-value">{vehicle.ownerId}</span>
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
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default ManageHousehold;