import React, { useState } from 'react'
import '../../styles/Resident.css'

const VehicleManagement = () => {
  const [activeTab, setActiveTab] = useState('register')
  const [vehicleData, setVehicleData] = useState({
    type: '',
    brand: '',
    model: '',
    color: '',
    licensePlate: '',
    registrationDate: ''
  })

  const handleSubmit = (e) => {
    e.preventDefault()
    // TODO: Implement vehicle registration
    console.log('Vehicle registration:', vehicleData)
  }

  return (
    <div className="vehicle-management">
      <h2>Quản lý phương tiện</h2>

      <div className="tabs">
        <button
          className={`tab ${activeTab === 'register' ? 'active' : ''}`}
          onClick={() => setActiveTab('register')}
        >
          Đăng ký phương tiện mới
        </button>
        <button
          className={`tab ${activeTab === 'list' ? 'active' : ''}`}
          onClick={() => setActiveTab('list')}
        >
          Danh sách phương tiện
        </button>
      </div>

      <div className="tab-content">
        {activeTab === 'register' && (
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Loại phương tiện:</label>
              <select
                value={vehicleData.type}
                onChange={(e) => setVehicleData({...vehicleData, type: e.target.value})}
              >
                <option value="">Chọn loại phương tiện</option>
                <option value="car">Ô tô</option>
                <option value="motorcycle">Xe máy</option>
                <option value="bicycle">Xe đạp</option>
                <option value="other">Khác</option>
              </select>
            </div>
            <div className="form-group">
              <label>Hãng xe:</label>
              <input
                type="text"
                value={vehicleData.brand}
                onChange={(e) => setVehicleData({...vehicleData, brand: e.target.value})}
              />
            </div>
            <div className="form-group">
              <label>Model:</label>
              <input
                type="text"
                value={vehicleData.model}
                onChange={(e) => setVehicleData({...vehicleData, model: e.target.value})}
              />
            </div>
            <div className="form-group">
              <label>Màu sắc:</label>
              <input
                type="text"
                value={vehicleData.color}
                onChange={(e) => setVehicleData({...vehicleData, color: e.target.value})}
              />
            </div>
            <div className="form-group">
              <label>Biển số xe:</label>
              <input
                type="text"
                value={vehicleData.licensePlate}
                onChange={(e) => setVehicleData({...vehicleData, licensePlate: e.target.value})}
              />
            </div>
            <div className="form-group">
              <label>Ngày đăng ký:</label>
              <input
                type="date"
                value={vehicleData.registrationDate}
                onChange={(e) => setVehicleData({...vehicleData, registrationDate: e.target.value})}
              />
            </div>
            <button type="submit" className="submit-button">
              Đăng ký phương tiện
            </button>
          </form>
        )}

        {activeTab === 'list' && (
          <div className="vehicle-list">
            <p>Chưa có phương tiện nào được đăng ký</p>
          </div>
        )}
      </div>
    </div>
  )
}

export default VehicleManagement 