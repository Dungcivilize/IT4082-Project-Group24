import React, { useState, useEffect } from 'react'
import { getUserProfile, updateUserProfile } from '../../api/profile'
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

  const [familyMembers, setFamilyMembers] = useState([
    {
      id: 1,
      fullName: 'Nguyễn Văn A',
      relationship: 'Con',
      dateOfBirth: '1990-01-01',
      gender: 'Nam',
      phone: '0123456789',
      idCard: '123456789'
    }
  ])

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

      // Thêm thông tin mật khẩu nếu đang thay đổi mật khẩu
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

  return (
    <div className="personal-info">
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
          Thành viên trong hộ
        </button>
      </div>

      {activeTab === 'owner' && (
        <div className="account-info">
          <h2>Thông tin tài khoản</h2>
          {error && <div className="error-message">{error}</div>}
          {success && <div className="success-message">{success}</div>}
          
          {!isEditing ? (
            <>
              <div className="account-info-row">
                <div className="info-item">
                  <label>Họ và tên:</label>
                  <span>{userProfile.fullName}</span>
                </div>
                <div className="info-item">
                  <label>Email:</label>
                  <span>{userProfile.email}</span>
                </div>
                <div className="info-item">
                  <label>Số điện thoại:</label>
                  <span>{userProfile.phone}</span>
                </div>
              </div>
              <div className="button-row">
                <button className="edit-button" onClick={() => setIsEditing(true)}>
                  Chỉnh sửa thông tin
                </button>
              </div>
            </>
          ) : (
            <form onSubmit={handleUpdateProfile}>
              <div className="form-section">
                <h3>Thông tin cá nhân</h3>
                <div className="form-row">
                  <div className="form-group">
                    <label>Họ và tên:</label>
                    <input
                      type="text"
                      value={userProfile.fullName}
                      disabled
                      className="disabled-input"
                    />
                  </div>
                  <div className="form-group">
                    <label>Email:</label>
                    <input
                      type="email"
                      value={userProfile.email}
                      onChange={(e) => setUserProfile({...userProfile, email: e.target.value})}
                    />
                  </div>
                  <div className="form-group">
                    <label>Số điện thoại:</label>
                    <input
                      type="tel"
                      value={userProfile.phone}
                      onChange={(e) => setUserProfile({...userProfile, phone: e.target.value})}
                    />
                  </div>
                </div>
              </div>

              <div className="form-section">
                <div className="password-header">
                  <h3>Đổi mật khẩu</h3>
                  <button 
                    type="button" 
                    className="toggle-password-button"
                    onClick={() => setShowPasswordSection(!showPasswordSection)}
                  >
                    {showPasswordSection ? 'Ẩn' : 'Hiện'}
                  </button>
                </div>
                
                {showPasswordSection && (
                  <div className="form-row">
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
                  </div>
                )}
              </div>

              <div className="button-row">
                <button type="submit" className="save-button">
                  Lưu thay đổi
                </button>
                <button type="button" className="cancel-button" onClick={handleCancel}>
                  Hủy
                </button>
              </div>
            </form>
          )}
        </div>
      )}

      {activeTab === 'members' && (
        <div className="members-info">
          <h2>Thành viên trong hộ</h2>
          <div className="members-list">
            {familyMembers.map(member => (
              <div key={member.id} className="member-card">
                <h3>{member.fullName}</h3>
                <div className="member-details">
                  <p><strong>Quan hệ:</strong> {member.relationship}</p>
                  <p><strong>Ngày sinh:</strong> {member.dateOfBirth}</p>
                  <p><strong>Giới tính:</strong> {member.gender}</p>
                  <p><strong>Số điện thoại:</strong> {member.phone}</p>
                  <p><strong>CMND/CCCD:</strong> {member.idCard}</p>
                </div>
                <div className="member-actions">
                  <button className="edit-button">Chỉnh sửa</button>
                  <button className="delete-button">Xóa</button>
                </div>
              </div>
            ))}
            <button className="add-member-button">
              <i className="fas fa-plus"></i> Thêm thành viên
            </button>
          </div>
        </div>
      )}
    </div>
  )
}

export default PersonalInfo 