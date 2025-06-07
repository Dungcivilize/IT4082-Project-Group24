import { Outlet, Link, useNavigate } from 'react-router-dom'
import '../../styles/ResidentLayout.css'

const ResidentLayout = () => {
  const navigate = useNavigate()

  const handleLogout = () => {
    localStorage.removeItem('user')
    localStorage.removeItem('token')
    navigate('/')
  }

  return (
    <div className="resident-layout">
      <nav className="sidebar">
        <h2>Menu Cư dân</h2>
        <ul>
          <li>
            <Link to="/resident/personal-info">Thông tin cá nhân</Link>
          </li>
          <li>
            <Link to="/resident/residency">Quản lý tạm trú/tạm vắng</Link>
          </li>
          <li>
            <Link to="/resident/vehicles">Quản lý phương tiện</Link>
          </li>
          <li>
            <Link to="/resident/payments">Quản lý thanh toán</Link>
          </li>
        </ul>
        <div className="sidebar-footer">
          <button className="logout-button" onClick={handleLogout}>
            Đăng xuất
          </button>
        </div>
      </nav>
      <main className="content">
        <Outlet />
      </main>
    </div>
  )
}

export default ResidentLayout 