import React, { useState, useEffect } from 'react'
import axios from 'axios'
import '../../styles/Resident.css'
import { API_URL } from '../../constants/api'

const ResidencyManagement = () => {
  const [activeTab, setActiveTab] = useState('temporary-residence')
  const [residents, setResidents] = useState([])
  const [temporaryResidents, setTemporaryResidents] = useState([])
  const [temporaryAbsents, setTemporaryAbsents] = useState([])
  const [selectedResident, setSelectedResident] = useState('')
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  
  // Thêm state cho phân trang và tab con trong history
  const [historySubTab, setHistorySubTab] = useState('temporary-residence')
  const [visibleTemporaryResidents, setVisibleTemporaryResidents] = useState(5)
  const [visibleTemporaryAbsents, setVisibleTemporaryAbsents] = useState(5)

  // Form cho đăng ký tạm trú
  const [temporaryResidentForm, setTemporaryResidentForm] = useState({
    fullName: '',
    birthDate: '',
    gender: 'male',
    identityCard: '',
    phone: '',
    startDate: '',
    endDate: '',
    reason: ''
  })

  // Form cho đăng ký tạm vắng
  const [temporaryAbsentForm, setTemporaryAbsentForm] = useState({
    startDate: '',
    endDate: '',
    temporaryAddress: '',
    reason: ''
  })

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    try {
      const user = JSON.parse(localStorage.getItem('user'))
      if (!user?.userId) {
        setError('Vui lòng đăng nhập để sử dụng tính năng này')
        return
      }

      try {
        // Tải danh sách tạm trú
        const temporaryResidentsResponse = await axios.get(`${API_URL}/residents/temporary/residents?userId=${user.userId}`)
        const sortedTemporaryResidents = Array.isArray(temporaryResidentsResponse.data) 
          ? temporaryResidentsResponse.data.sort((a, b) => new Date(b.startDate) - new Date(a.startDate))
          : []
        setTemporaryResidents(sortedTemporaryResidents)
      } catch (error) {
        console.error('Lỗi khi tải danh sách tạm trú:', error.response || error)
        setTemporaryResidents([])
      }

      try {
        // Tải danh sách tạm vắng
        const temporaryAbsentsResponse = await axios.get(`${API_URL}/residents/temporary/absents?userId=${user.userId}`)
        const sortedTemporaryAbsents = Array.isArray(temporaryAbsentsResponse.data)
          ? temporaryAbsentsResponse.data.sort((a, b) => new Date(b.startDate) - new Date(a.startDate))
          : []
        setTemporaryAbsents(sortedTemporaryAbsents)
      } catch (error) {
        console.error('Lỗi khi tải danh sách tạm vắng:', error.response || error)
        setTemporaryAbsents([])
      }

      // Tải danh sách cư dân của căn hộ (để chọn khi đăng ký tạm vắng)
      try {
        const residentsResponse = await axios.get(`${API_URL}/residents/user/${user.userId}`)
        setResidents(Array.isArray(residentsResponse.data) ? residentsResponse.data : [])
      } catch (error) {
        console.error('Lỗi khi tải danh sách cư dân:', error.response || error)
        setResidents([])
      }

      setError('')
    } catch (error) {
      console.error('Lỗi khi tải dữ liệu:', error.response || error)
      setError(error.response?.data?.message || 'Có lỗi xảy ra khi tải dữ liệu')
      setResidents([])
      setTemporaryResidents([])
      setTemporaryAbsents([])
    }
  }

  const handleTemporaryResidentSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')

    try {
      const user = JSON.parse(localStorage.getItem('user'))
      if (!user?.userId) {
        setError('Vui lòng đăng nhập để sử dụng tính năng này')
        return
      }

      // Format lại ngày tháng trước khi gửi
      const formattedData = {
        ...temporaryResidentForm,
        birthDate: temporaryResidentForm.birthDate ? new Date(temporaryResidentForm.birthDate).toISOString().split('T')[0] : null,
        startDate: temporaryResidentForm.startDate ? new Date(temporaryResidentForm.startDate).toISOString().split('T')[0] : null,
        endDate: temporaryResidentForm.endDate ? new Date(temporaryResidentForm.endDate).toISOString().split('T')[0] : null
      }

      await axios.post(`${API_URL}/residents/temporary/residents?userId=${user.userId}`, formattedData)
      
      setSuccess('Đăng ký tạm trú thành công')
      setTemporaryResidentForm({
        fullName: '',
        birthDate: '',
        gender: 'male',
        identityCard: '',
        phone: '',
        startDate: '',
        endDate: '',
        reason: ''
      })
      loadData()
    } catch (error) {
      setError(error.response?.data?.message || 'Có lỗi xảy ra khi đăng ký tạm trú')
    }
  }

  const handleTemporaryAbsentSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')

    try {
      const user = JSON.parse(localStorage.getItem('user'))
      if (!user?.userId) {
        setError('Vui lòng đăng nhập để sử dụng tính năng này')
        return
      }

      if (!selectedResident) {
        setError('Vui lòng chọn thành viên cần đăng ký tạm vắng')
        return
      }

      // Format lại ngày tháng trước khi gửi
      const formattedData = {
        ...temporaryAbsentForm,
        startDate: temporaryAbsentForm.startDate ? new Date(temporaryAbsentForm.startDate).toISOString().split('T')[0] : null,
        endDate: temporaryAbsentForm.endDate ? new Date(temporaryAbsentForm.endDate).toISOString().split('T')[0] : null
      }

      await axios.post(
        `${API_URL}/residents/temporary/absents/${selectedResident}?userId=${user.userId}`,
        formattedData
      )
      
      setSuccess('Đăng ký tạm vắng thành công')
      setTemporaryAbsentForm({
        startDate: '',
        endDate: '',
        temporaryAddress: '',
        reason: ''
      })
      setSelectedResident('')
      loadData()
    } catch (error) {
      setError(error.response?.data?.message || 'Có lỗi xảy ra khi đăng ký tạm vắng')
    }
  }

  const handleDeleteTemporaryAbsent = async (temporaryAbsentId) => {
    if (!window.confirm('Bạn có chắc chắn muốn xóa đăng ký tạm vắng này?')) return
    
    setError('')
    setSuccess('')

    try {
      const user = JSON.parse(localStorage.getItem('user'))
      if (!user?.userId) {
        setError('Vui lòng đăng nhập để sử dụng tính năng này')
        return
      }

      await axios.delete(`${API_URL}/residents/temporary/absents/${temporaryAbsentId}?userId=${user.userId}`)
      setSuccess('Xóa đăng ký tạm vắng thành công')
      loadData()
    } catch (error) {
      setError(error.response?.data?.message || 'Có lỗi xảy ra khi xóa đăng ký tạm vắng')
    }
  }

  const formatDate = (dateString) => {
    if (!dateString) return ''
    const date = new Date(dateString)
    return date.toLocaleDateString('vi-VN')
  }

  return (
    <div className="residency-management">
      <h2>Quản lý tạm trú/tạm vắng</h2>
      
      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}
      
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
        {activeTab === 'temporary-residence' && (
          <form onSubmit={handleTemporaryResidentSubmit} className="residency-form">
            <div className="form-group">
              <label>Họ và tên:</label>
              <input
                type="text"
                value={temporaryResidentForm.fullName}
                onChange={(e) => setTemporaryResidentForm({...temporaryResidentForm, fullName: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Ngày sinh:</label>
              <input
                type="date"
                value={temporaryResidentForm.birthDate}
                onChange={(e) => setTemporaryResidentForm({...temporaryResidentForm, birthDate: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Giới tính:</label>
              <select
                value={temporaryResidentForm.gender}
                onChange={(e) => setTemporaryResidentForm({...temporaryResidentForm, gender: e.target.value})}
                required
              >
                <option value="male">Nam</option>
                <option value="female">Nữ</option>
                <option value="other">Khác</option>
              </select>
            </div>
            <div className="form-group">
              <label>CMND/CCCD:</label>
              <input
                type="text"
                value={temporaryResidentForm.identityCard}
                onChange={(e) => setTemporaryResidentForm({...temporaryResidentForm, identityCard: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Số điện thoại:</label>
              <input
                type="tel"
                value={temporaryResidentForm.phone}
                onChange={(e) => setTemporaryResidentForm({...temporaryResidentForm, phone: e.target.value})}
              />
            </div>
            <div className="form-group">
              <label>Ngày bắt đầu:</label>
              <input
                type="date"
                value={temporaryResidentForm.startDate}
                onChange={(e) => setTemporaryResidentForm({...temporaryResidentForm, startDate: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Ngày kết thúc:</label>
              <input
                type="date"
                value={temporaryResidentForm.endDate}
                onChange={(e) => setTemporaryResidentForm({...temporaryResidentForm, endDate: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Lý do:</label>
              <textarea
                value={temporaryResidentForm.reason}
                onChange={(e) => setTemporaryResidentForm({...temporaryResidentForm, reason: e.target.value})}
                required
              />
            </div>
            <button type="submit" className="submit-button">
              Đăng ký tạm trú
            </button>
          </form>
        )}

        {activeTab === 'temporary-absence' && (
          <form onSubmit={handleTemporaryAbsentSubmit} className="residency-form">
            <div className="form-group">
              <label>Chọn thành viên:</label>
              <select
                value={selectedResident}
                onChange={(e) => setSelectedResident(e.target.value)}
                required
              >
                <option value="">-- Chọn thành viên --</option>
                {Array.isArray(residents) && residents.map(resident => (
                  <option key={resident.residentId} value={resident.residentId}>
                    {resident.fullName}
                  </option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label>Ngày bắt đầu:</label>
              <input
                type="date"
                value={temporaryAbsentForm.startDate}
                onChange={(e) => setTemporaryAbsentForm({...temporaryAbsentForm, startDate: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Ngày kết thúc:</label>
              <input
                type="date"
                value={temporaryAbsentForm.endDate}
                onChange={(e) => setTemporaryAbsentForm({...temporaryAbsentForm, endDate: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Địa chỉ tạm trú:</label>
              <input
                type="text"
                value={temporaryAbsentForm.temporaryAddress}
                onChange={(e) => setTemporaryAbsentForm({...temporaryAbsentForm, temporaryAddress: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Lý do:</label>
              <textarea
                value={temporaryAbsentForm.reason}
                onChange={(e) => setTemporaryAbsentForm({...temporaryAbsentForm, reason: e.target.value})}
                required
              />
            </div>
            <button type="submit" className="submit-button">
              Đăng ký tạm vắng
            </button>
          </form>
        )}

        {activeTab === 'history' && (
          <div className="history-section">
            <div className="history-tabs">
              <button
                className={`history-tab ${historySubTab === 'temporary-residence' ? 'active' : ''}`}
                onClick={() => setHistorySubTab('temporary-residence')}
              >
                Danh sách tạm trú
              </button>
              <button
                className={`history-tab ${historySubTab === 'temporary-absence' ? 'active' : ''}`}
                onClick={() => setHistorySubTab('temporary-absence')}
              >
                Danh sách tạm vắng
              </button>
            </div>

            {historySubTab === 'temporary-residence' && (
              <div className="history-group">
                <h3>Danh sách tạm trú</h3>
                <div className="history-list">
                  {!Array.isArray(temporaryResidents) || temporaryResidents.length === 0 ? (
                    <p>Chưa có đăng ký tạm trú</p>
                  ) : (
                    <>
                      {temporaryResidents.slice(0, visibleTemporaryResidents).map(tr => (
                        <div key={tr.temporaryResidentId} className="history-item">
                          <h4>{tr.fullName}</h4>
                          <p>CCCD/CMND: {tr.identityCard}</p>
                          <p>Ngày sinh: {formatDate(tr.birthDate)}</p>
                          <p>Giới tính: {tr.gender === 'male' ? 'Nam' : tr.gender === 'female' ? 'Nữ' : 'Khác'}</p>
                          <p>Số điện thoại: {tr.phone}</p>
                          <p>Thời gian: {formatDate(tr.startDate)} - {formatDate(tr.endDate)}</p>
                          <p>Lý do: {tr.reason}</p>
                        </div>
                      ))}
                      {temporaryResidents.length > visibleTemporaryResidents && (
                        <button
                          className="load-more-button"
                          onClick={() => setVisibleTemporaryResidents(prev => prev + 5)}
                        >
                          Xem thêm
                        </button>
                      )}
                    </>
                  )}
                </div>
              </div>
            )}

            {historySubTab === 'temporary-absence' && (
              <div className="history-group">
                <h3>Danh sách tạm vắng</h3>
                <div className="history-list">
                  {!Array.isArray(temporaryAbsents) || temporaryAbsents.length === 0 ? (
                    <p>Chưa có đăng ký tạm vắng</p>
                  ) : (
                    <>
                      {temporaryAbsents.slice(0, visibleTemporaryAbsents).map(ta => (
                        <div key={ta.temporaryAbsentId} className="history-item">
                          <h4>{ta.residentName}</h4>
                          <p>Địa chỉ tạm trú: {ta.temporaryAddress}</p>
                          <p>Thời gian: {formatDate(ta.startDate)} - {formatDate(ta.endDate)}</p>
                          <p>Lý do: {ta.reason}</p>
                          <button 
                            onClick={() => handleDeleteTemporaryAbsent(ta.temporaryAbsentId)}
                            className="delete-button"
                          >
                            Xóa
                          </button>
                        </div>
                      ))}
                      {temporaryAbsents.length > visibleTemporaryAbsents && (
                        <button
                          className="load-more-button"
                          onClick={() => setVisibleTemporaryAbsents(prev => prev + 5)}
                        >
                          Xem thêm
                        </button>
                      )}
                    </>
                  )}
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  )
}

export default ResidencyManagement 