import React, { useState, useEffect } from 'react'
import { getUserProfile, updateUserProfile } from '../../api/profile'
import { getResidentsByUser, addResident, updateResident, deleteResident } from '../../api/resident'
import '../../styles/Resident.css'

const PersonalInfo = () => {
  const [activeTab, setActiveTab] = useState('owner')
  const [isEditing, setIsEditing] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [userProfile, setUserProfile] = useState({
    userId: null,
    email: '',
    fullName: '',
    phone: '',
  })
  const [passwordData, setPasswordData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  })
  const [showPasswordSection, setShowPasswordSection] = useState(false)

  const [residents, setResidents] = useState([])
  const [isAddingResident, setIsAddingResident] = useState(false)
  const [editingResidentId, setEditingResidentId] = useState(null)
  const [residentFormData, setResidentFormData] = useState({
    fullName: '',
    birthDate: '',
    gender: 'male',
    identityCard: '',
    phone: '',
    email: '',
    occupation: '',
    residentType: 'member',
    relationship: '',
    status: 'living'
  })

  useEffect(() => {
    const loadUserProfile = async () => {
      try {
        const user = JSON.parse(localStorage.getItem('user'))
        if (!user?.userId) {
          setError('Không tìm thấy thông tin người dùng')
          return
        }

        const profile = await getUserProfile(user.userId)
        setUserProfile({
          userId: user.userId,
          email: profile.email,
          fullName: profile.fullName,
          phone: profile.phone
        })

        const residentsData = await getResidentsByUser(user.userId)
        setResidents(residentsData)
      } catch (error) {
        setError('Có lỗi khi tải thông tin người dùng')
      }
    }

    loadUserProfile()
  }, [])

  const handleUpdateProfile = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')

    if (!userProfile.userId) {
      setError('Không tìm thấy thông tin người dùng')
      return
    }

    try {
      const updatedData = {
        email: userProfile.email,
        phone: userProfile.phone
      }

      if (showPasswordSection && passwordData.currentPassword && passwordData.newPassword) {
        if (passwordData.newPassword !== passwordData.confirmPassword) {
          setError('Mật khẩu mới không khớp')
          return
        }
        updatedData.currentPassword = passwordData.currentPassword
        updatedData.newPassword = passwordData.newPassword
      }
      
      const response = await updateUserProfile(userProfile.userId, updatedData)
      if (response) {
        setUserProfile({
          ...userProfile,
          email: response.email,
          phone: response.phone
        })
        setSuccess('Cập nhật thông tin thành công')
        setIsEditing(false)
        setShowPasswordSection(false)
        setPasswordData({
          currentPassword: '',
          newPassword: '',
          confirmPassword: ''
        })
      }
    } catch (error) {
      setError('Có lỗi xảy ra khi cập nhật thông tin')
    }
  }

  const handleCancel = () => {
    setIsEditing(false)
    setShowPasswordSection(false)
    setPasswordData({
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    })
  }

  const handleAddResident = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')

    try {
      const response = await addResident(userProfile.userId, residentFormData)
      setResidents([...residents, response])
      setSuccess('Thêm thành viên thành công')
      setIsAddingResident(false)
      resetFormData()
    } catch (error) {
      setError(error.toString())
    }
  }

  const handleUpdateResident = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')

    try {
      const response = await updateResident(userProfile.userId, editingResidentId, residentFormData)
      setResidents(residents.map(resident => 
        resident.residentId === editingResidentId ? response : resident
      ))
      setSuccess('Cập nhật thông tin thành công')
      setEditingResidentId(null)
      resetFormData()
    } catch (error) {
      setError(error.toString())
    }
  }

  const handleDeleteResident = async (residentId) => {
    if (!window.confirm('Bạn có chắc chắn muốn xóa thành viên này?')) {
      return
    }

    setError('')
    setSuccess('')

    try {
      await deleteResident(userProfile.userId, residentId)
      setResidents(residents.filter(resident => resident.residentId !== residentId))
      setSuccess('Xóa thành viên thành công')
    } catch (error) {
      setError(error.toString())
    }
  }

  const resetFormData = () => {
    setResidentFormData({
      fullName: '',
      birthDate: '',
      gender: 'male',
      identityCard: '',
      phone: '',
      email: '',
      occupation: '',
      residentType: 'member',
      relationship: '',
      status: 'living'
    })
  }

  const cancelResidentForm = () => {
    setIsAddingResident(false)
    setEditingResidentId(null)
    resetFormData()
  }

  const startEditResident = (resident) => {
    setEditingResidentId(resident.residentId)
    setResidentFormData({
      fullName: resident.fullName,
      birthDate: resident.birthDate,
      gender: resident.gender,
      identityCard: resident.identityCard,
      phone: resident.phone,
      email: resident.email,
      occupation: resident.occupation,
      residentType: resident.residentType,
      relationship: resident.relationship,
      status: resident.status
    })
  }

  const handleResidentChange = (e) => {
    const { name, value } = e.target;
    
    // Xử lý đặc biệt cho trường birthDate
    if (name === 'birthDate') {
      // Đảm bảo ngày được format theo chuẩn ISO (YYYY-MM-DD)
      const dateValue = value ? new Date(value).toISOString().split('T')[0] : '';
      setResidentFormData({
        ...residentFormData,
        [name]: dateValue
      });
    } else {
      setResidentFormData({
        ...residentFormData,
        [name]: value
      });
    }
  };

  const formatDateForDisplay = (dateString) => {
    if (!dateString) return '';
    // Chuyển đổi từ ISO date string sang định dạng hiển thị DD/MM/YYYY
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  };

  const ResidentForm = ({ onSubmit, isEditing }) => (
    <form onSubmit={onSubmit} className="resident-form">
      <div className="form-row">
        <div className="form-group">
          <label>Họ và tên:</label>
          <input
            type="text"
            name="fullName"
            value={residentFormData.fullName}
            onChange={handleResidentChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Ngày sinh:</label>
          <input
            type="date"
            name="birthDate"
            value={residentFormData.birthDate || ''}
            onChange={handleResidentChange}
            max={new Date().toISOString().split('T')[0]} // Giới hạn không cho chọn ngày trong tương lai
            required
          />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Giới tính:</label>
          <select
            name="gender"
            value={residentFormData.gender}
            onChange={handleResidentChange}
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
            name="identityCard"
            value={residentFormData.identityCard}
            onChange={handleResidentChange}
            required
          />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Số điện thoại:</label>
          <input
            type="tel"
            name="phone"
            value={residentFormData.phone}
            onChange={handleResidentChange}
          />
        </div>
        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={residentFormData.email}
            onChange={handleResidentChange}
          />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>Nghề nghiệp:</label>
          <input
            type="text"
            name="occupation"
            value={residentFormData.occupation}
            onChange={handleResidentChange}
          />
        </div>
        <div className="form-group">
          <label>Loại cư dân:</label>
          <select
            name="residentType"
            value={residentFormData.residentType}
            onChange={handleResidentChange}
          >
            <option value="owner">Chủ hộ</option>
            <option value="member">Thành viên</option>
          </select>
        </div>
      </div>

      {residentFormData.residentType === 'member' && (
        <div className="form-group">
          <label>Quan hệ với chủ hộ:</label>
          <input
            type="text"
            name="relationship"
            value={residentFormData.relationship}
            onChange={handleResidentChange}
            required
          />
        </div>
      )}

      <div className="form-row">
        <div className="form-group">
          <label>Trạng thái:</label>
          <select
            name="status"
            value={residentFormData.status}
            onChange={handleResidentChange}
          >
            <option value="living">Đang sinh sống</option>
            <option value="moved_out">Đã chuyển đi</option>
            <option value="temporary_absent">Tạm vắng</option>
          </select>
        </div>
      </div>

      <div className="button-row">
        <button type="submit" className="save-button">
          {isEditing ? 'Cập nhật' : 'Thêm thành viên'}
        </button>
        <button type="button" className="cancel-button" onClick={cancelResidentForm}>
          Hủy
        </button>
      </div>
    </form>
  )

  return (
    <div className="personal-info-container">
      <div className="tabs">
        <button 
          className={`tab ${activeTab === 'owner' ? 'active' : ''}`}
          onClick={() => setActiveTab('owner')}
        >
          Thông tin chủ hộ
        </button>
        <button 
          className={`tab ${activeTab === 'members' ? 'active' : ''}`}
          onClick={() => setActiveTab('members')}
        >
          Thông tin thành viên
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}

      {activeTab === 'owner' && (
        <div className="profile-section">
          <form onSubmit={handleUpdateProfile}>
            <div className="form-group">
              <label>Email:</label>
              <input
                type="email"
                value={userProfile.email}
                onChange={(e) => setUserProfile({...userProfile, email: e.target.value})}
                disabled={!isEditing}
              />
            </div>
            <div className="form-group">
              <label>Họ và tên:</label>
              <input
                type="text"
                value={userProfile.fullName}
                disabled
              />
            </div>
            <div className="form-group">
              <label>Số điện thoại:</label>
              <input
                type="tel"
                value={userProfile.phone}
                onChange={(e) => setUserProfile({...userProfile, phone: e.target.value})}
                disabled={!isEditing}
              />
            </div>

            {showPasswordSection && (
              <>
                <div className="form-group">
                  <label>Mật khẩu hiện tại:</label>
                  <input
                    type="password"
                    value={passwordData.currentPassword}
                    onChange={(e) => setPasswordData({...passwordData, currentPassword: e.target.value})}
                  />
                </div>
                <div className="form-group">
                  <label>Mật khẩu mới:</label>
                  <input
                    type="password"
                    value={passwordData.newPassword}
                    onChange={(e) => setPasswordData({...passwordData, newPassword: e.target.value})}
                  />
                </div>
                <div className="form-group">
                  <label>Xác nhận mật khẩu mới:</label>
                  <input
                    type="password"
                    value={passwordData.confirmPassword}
                    onChange={(e) => setPasswordData({...passwordData, confirmPassword: e.target.value})}
                  />
                </div>
              </>
            )}

            <div className="button-group">
              {!isEditing ? (
                <button type="button" onClick={() => setIsEditing(true)}>
                  Chỉnh sửa
                </button>
              ) : (
                <>
                  {!showPasswordSection && (
                    <button type="button" onClick={() => setShowPasswordSection(true)}>
                      Đổi mật khẩu
                    </button>
                  )}
                  <button type="submit">Lưu thay đổi</button>
                  <button type="button" onClick={handleCancel}>
                    Hủy
                  </button>
                </>
              )}
            </div>
          </form>
        </div>
      )}

      {activeTab === 'members' && (
        <div className="members-section">
          {!isAddingResident && !editingResidentId && (
            <button onClick={() => setIsAddingResident(true)} className="add-button">
              Thêm thành viên
            </button>
          )}

          {(isAddingResident || editingResidentId) && (
            <form onSubmit={editingResidentId ? handleUpdateResident : handleAddResident} className="resident-form">
              <div className="form-group">
                <label>Họ và tên:</label>
                <input
                  type="text"
                  name="fullName"
                  value={residentFormData.fullName}
                  onChange={handleResidentChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Ngày sinh:</label>
                <input
                  type="date"
                  name="birthDate"
                  value={residentFormData.birthDate || ''}
                  onChange={handleResidentChange}
                  max={new Date().toISOString().split('T')[0]} // Giới hạn không cho chọn ngày trong tương lai
                  required
                />
              </div>
              <div className="form-group">
                <label>Giới tính:</label>
                <select
                  name="gender"
                  value={residentFormData.gender}
                  onChange={handleResidentChange}
                >
                  <option value="male">Nam</option>
                  <option value="female">Nữ</option>
                  <option value="other">Khác</option>
                </select>
              </div>
              <div className="form-group">
                <label>CCCD/CMND:</label>
                <input
                  type="text"
                  name="identityCard"
                  value={residentFormData.identityCard}
                  onChange={handleResidentChange}
                  required
                />
              </div>
              <div className="form-group">
                <label>Số điện thoại:</label>
                <input
                  type="tel"
                  name="phone"
                  value={residentFormData.phone}
                  onChange={handleResidentChange}
                />
              </div>
              <div className="form-group">
                <label>Email:</label>
                <input
                  type="email"
                  name="email"
                  value={residentFormData.email}
                  onChange={handleResidentChange}
                />
              </div>
              <div className="form-group">
                <label>Nghề nghiệp:</label>
                <input
                  type="text"
                  name="occupation"
                  value={residentFormData.occupation}
                  onChange={handleResidentChange}
                />
              </div>
              <div className="form-group">
                <label>Loại cư dân:</label>
                <select
                  name="residentType"
                  value={residentFormData.residentType}
                  onChange={handleResidentChange}
                >
                  <option value="owner">Chủ hộ</option>
                  <option value="member">Thành viên</option>
                </select>
              </div>
              
              {residentFormData.residentType === 'member' && (
                <div className="form-group">
                  <label>Quan hệ với chủ hộ:</label>
                  <input
                    type="text"
                    name="relationship"
                    value={residentFormData.relationship}
                    onChange={handleResidentChange}
                    required
                  />
                </div>
              )}

              <div className="form-group">
                <label>Trạng thái:</label>
                <select
                  name="status"
                  value={residentFormData.status}
                  onChange={handleResidentChange}
                >
                  <option value="living">Đang sinh sống</option>
                  <option value="moved_out">Đã chuyển đi</option>
                  <option value="temporary_absent">Tạm vắng</option>
                </select>
              </div>

              <div className="button-group">
                <button type="submit">
                  {editingResidentId ? 'Cập nhật' : 'Thêm'}
                </button>
                <button type="button" onClick={cancelResidentForm}>
                  Hủy
                </button>
              </div>
            </form>
          )}

          <div className="residents-list">
            {residents.map(resident => (
              <div key={resident.residentId} className="resident-card">
                <h3>{resident.fullName}</h3>
                <p>Ngày sinh: {formatDateForDisplay(resident.birthDate)}</p>
                <p>Giới tính: {resident.gender === 'male' ? 'Nam' : resident.gender === 'female' ? 'Nữ' : 'Khác'}</p>
                <p>CCCD/CMND: {resident.identityCard}</p>
                <p>Số điện thoại: {resident.phone}</p>
                <p>Email: {resident.email}</p>
                <p>Nghề nghiệp: {resident.occupation}</p>
                <p>Loại cư dân: {resident.residentType === 'owner' ? 'Chủ hộ' : 'Thành viên'}</p>
                {resident.residentType === 'member' && (
                  <p>Quan hệ với chủ hộ: {resident.relationship}</p>
                )}
                <p>Trạng thái: {
                  resident.status === 'living' ? 'Đang sinh sống' : 
                  resident.status === 'moved_out' ? 'Đã chuyển đi' : 
                  'Tạm vắng'
                }</p>
                
                <div className="button-group">
                  <button onClick={() => startEditResident(resident)}>Sửa</button>
                  <button onClick={() => handleDeleteResident(resident.residentId)}>Xóa</button>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}

export default PersonalInfo 