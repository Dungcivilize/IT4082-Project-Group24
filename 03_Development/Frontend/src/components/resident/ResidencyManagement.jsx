import React, { useState } from 'react'
import '../../styles/Resident.css'

const ResidencyManagement = () => {
  const [activeTab, setActiveTab] = useState('temporary-residence')
  const [formData, setFormData] = useState({
    fullName: '',
    idCard: '',
    phone: '',
    startDate: '',
    endDate: '',
    reason: ''
  })

  const handleSubmit = (e) => {
    e.preventDefault()
    // TODO: Implement form submission
    console.log('Form submitted:', formData)
  }

  return (
    <div className="residency-management">
      <h2>Quản lý tạm trú/tạm vắng</h2>
      
      <div className="tabs">
        <button
          className={`tab ${activeTab === 'temporary-residence' ? 'active' : ''}`}
          onClick={() => setActiveTab('temporary-residence')}
        >
          Đăng ký tạm trú
        </button>
        <button
          className={`tab ${activeTab === 'temporary-absence' ? 'active' : ''}`}
          onClick={() => setActiveTab('temporary-absence')}
        >
          Đăng ký tạm vắng
        </button>
        <button
          className={`tab ${activeTab === 'history' ? 'active' : ''}`}
          onClick={() => setActiveTab('history')}
        >
          Lịch sử đăng ký
        </button>
      </div>

      <div className="tab-content">
        {activeTab !== 'history' && (
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Họ và tên:</label>
              <input
                type="text"
                value={formData.fullName}
                onChange={(e) => setFormData({...formData, fullName: e.target.value})}
              />
            </div>
            <div className="form-group">
              <label>CMND/CCCD:</label>
              <input
                type="text"
                value={formData.idCard}
                onChange={(e) => setFormData({...formData, idCard: e.target.value})}
              />
            </div>
            <div className="form-group">
              <label>Số điện thoại:</label>
              <input
                type="tel"
                value={formData.phone}
                onChange={(e) => setFormData({...formData, phone: e.target.value})}
              />
            </div>
            <div className="form-group">
              <label>Ngày bắt đầu:</label>
              <input
                type="date"
                value={formData.startDate}
                onChange={(e) => setFormData({...formData, startDate: e.target.value})}
              />
            </div>
            <div className="form-group">
              <label>Ngày kết thúc:</label>
              <input
                type="date"
                value={formData.endDate}
                onChange={(e) => setFormData({...formData, endDate: e.target.value})}
              />
            </div>
            <div className="form-group">
              <label>Lý do:</label>
              <textarea
                value={formData.reason}
                onChange={(e) => setFormData({...formData, reason: e.target.value})}
              />
            </div>
            <button type="submit" className="submit-button">
              {activeTab === 'temporary-residence' ? 'Đăng ký tạm trú' : 'Đăng ký tạm vắng'}
            </button>
          </form>
        )}

        {activeTab === 'history' && (
          <div className="history-list">
            <p>Chưa có lịch sử đăng ký</p>
          </div>
        )}
      </div>
    </div>
  )
}

export default ResidencyManagement 