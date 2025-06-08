import React, { useState, useEffect } from 'react'
import axios from 'axios'
import { API_URL } from '../../constants/api'
import '../../styles/Resident.css'

const VehicleManagement = () => {
  const [activeTab, setActiveTab] = useState('register')
  const [vehicles, setVehicles] = useState([])
  const [residents, setResidents] = useState([])
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [vehicleData, setVehicleData] = useState({
    type: '',
    licensePlate: '',
    ownerId: ''
  })
  const [editingVehicleId, setEditingVehicleId] = useState(null)

  useEffect(() => {
    loadVehicles()
    loadResidents()
  }, [])

  const loadVehicles = async () => {
    try {
      const user = JSON.parse(localStorage.getItem('user'))
      if (!user?.userId) {
        setError('Vui lòng đăng nhập để sử dụng tính năng này')
        return
      }

      const response = await axios.get(`${API_URL}/residents/vehicles?userId=${user.userId}`)
      setVehicles(response.data)
      setError('')
    } catch (error) {
      console.error('Lỗi khi tải danh sách phương tiện:', error)
      setError(error.response?.data || 'Có lỗi xảy ra khi tải danh sách phương tiện')
    }
  }

  const loadResidents = async () => {
    try {
      const user = JSON.parse(localStorage.getItem('user'))
      if (!user?.userId) return

      const response = await axios.get(`${API_URL}/residents/user/${user.userId}`)
      setResidents(response.data)
    } catch (error) {
      console.error('Lỗi khi tải danh sách cư dân:', error)
      setError(error.response?.data || 'Có lỗi xảy ra khi tải danh sách cư dân')
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')

    if (!vehicleData.ownerId) {
      setError('Vui lòng chọn chủ xe')
      return
    }

    try {
      const user = JSON.parse(localStorage.getItem('user'))
      if (!user?.userId) {
        setError('Vui lòng đăng nhập để sử dụng tính năng này')
        return
      }

      if (editingVehicleId) {
        await axios.put(
          `${API_URL}/residents/vehicles/${editingVehicleId}?userId=${user.userId}`,
          vehicleData
        )
        setSuccess('Cập nhật phương tiện thành công')
      } else {
        await axios.post(
          `${API_URL}/residents/vehicles?userId=${user.userId}`,
          vehicleData
        )
        setSuccess('Đăng ký phương tiện thành công')
      }

      setVehicleData({ type: '', licensePlate: '', ownerId: '' })
      setEditingVehicleId(null)
      loadVehicles()
      setActiveTab('list')
    } catch (error) {
      setError(error.response?.data || 'Có lỗi xảy ra khi xử lý yêu cầu')
    }
  }

  const handleEdit = (vehicle) => {
    setVehicleData({
      type: vehicle.type,
      licensePlate: vehicle.licensePlate,
      ownerId: vehicle.ownerId
    })
    setEditingVehicleId(vehicle.vehicleId)
    setActiveTab('register')
  }

  const handleDelete = async (vehicleId) => {
    if (!window.confirm('Bạn có chắc chắn muốn xóa phương tiện này?')) {
      return
    }

    try {
      const user = JSON.parse(localStorage.getItem('user'))
      if (!user?.userId) {
        setError('Vui lòng đăng nhập để sử dụng tính năng này')
        return
      }

      await axios.delete(`${API_URL}/residents/vehicles/${vehicleId}?userId=${user.userId}`)
      setSuccess('Xóa phương tiện thành công')
      loadVehicles()
    } catch (error) {
      setError(error.response?.data || 'Có lỗi xảy ra khi xóa phương tiện')
    }
  }

  const handleCancel = () => {
    setVehicleData({ type: '', licensePlate: '', ownerId: '' })
    setEditingVehicleId(null)
  }

  return (
    <div className="vehicle-management">
      <h2>Quản lý phương tiện</h2>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}

      <div className="tabs">
        <button
          className={`tab ${activeTab === 'register' ? 'active' : ''}`}
          onClick={() => setActiveTab('register')}
        >
          {editingVehicleId ? 'Cập nhật phương tiện' : 'Đăng ký phương tiện mới'}
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
          <form onSubmit={handleSubmit} className="vehicle-form">
            <div className="form-group">
              <label>Loại phương tiện:</label>
              <select
                value={vehicleData.type}
                onChange={(e) => setVehicleData({...vehicleData, type: e.target.value})}
                required
              >
                <option value="">Chọn loại phương tiện</option>
                <option value="car">Ô tô</option>
                <option value="motorcycle">Xe máy</option>
                <option value="bicycle">Xe đạp</option>
              </select>
            </div>

            <div className="form-group">
              <label>Biển số xe:</label>
              <input
                type="text"
                value={vehicleData.licensePlate}
                onChange={(e) => setVehicleData({...vehicleData, licensePlate: e.target.value})}
                required
                placeholder="Nhập biển số xe"
              />
            </div>

            <div className="form-group">
              <label>Chủ xe:</label>
              <select
                value={vehicleData.ownerId}
                onChange={(e) => setVehicleData({...vehicleData, ownerId: e.target.value})}
                required
              >
                <option value="">Chọn chủ xe</option>
                {residents.map(resident => (
                  <option key={resident.residentId} value={resident.residentId}>
                    {resident.fullName} - {resident.residentType === 'owner' ? 'Chủ hộ' : 'Thành viên'}
                  </option>
                ))}
              </select>
            </div>

            <div className="button-group">
              <button type="submit" className="submit-button">
                {editingVehicleId ? 'Cập nhật' : 'Đăng ký'}
              </button>
              {(editingVehicleId || vehicleData.type || vehicleData.licensePlate || vehicleData.ownerId) && (
                <button type="button" className="cancel-button" onClick={handleCancel}>
                  Hủy
                </button>
              )}
            </div>
          </form>
        )}

        {activeTab === 'list' && (
          <div className="vehicle-list">
            {vehicles.length === 0 ? (
              <p>Chưa có phương tiện nào được đăng ký</p>
            ) : (
              <div className="vehicle-grid">
                {vehicles.map(vehicle => (
                  <div key={vehicle.vehicleId} className="vehicle-card">
                    <h3>Biển số: {vehicle.licensePlate}</h3>
                    <p>Loại xe: {
                      vehicle.type === 'car' ? 'Ô tô' :
                      vehicle.type === 'motorcycle' ? 'Xe máy' :
                      vehicle.type === 'bicycle' ? 'Xe đạp' : 'Khác'
                    }</p>
                    <p>Căn hộ: {vehicle.apartmentCode}</p>
                    <p>Chủ xe: {vehicle.ownerName}</p>
                    <div className="button-group">
                      <button onClick={() => handleEdit(vehicle)}>Sửa</button>
                      <button onClick={() => handleDelete(vehicle.vehicleId)}>Xóa</button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  )
}

export default VehicleManagement 