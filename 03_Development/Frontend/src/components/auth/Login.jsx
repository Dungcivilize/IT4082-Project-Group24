import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { login } from '../../api/auth'
import '../../styles/Login.css'

const Login = ({ onClose }) => {
  const navigate = useNavigate()
  const [credentials, setCredentials] = useState({
    email: '',
    password: ''
  })
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  const handleLogin = async (e) => {
    e.preventDefault()
    setError('')
    setIsLoading(true)

    try {
      const response = await login(credentials.email, credentials.password)
      localStorage.setItem('user', JSON.stringify(response))
      
      switch (response.role) {
        case 'resident':
          navigate('/resident/personal-info')
          break
        case 'admin':
          navigate('/admin')
          break
        case 'accountant':
          navigate('/accountant')
          break
        default:
          navigate('/')
      }
    } catch (error) {
      setError(error.message)
    } finally {
      setIsLoading(false)
    }
  }

  const handleClose = (e) => {
    // Ngăn sự kiện click từ việc lan truyền đến container
    e.stopPropagation()
    if (onClose) onClose()
  }

  const handleContainerClick = (e) => {
    // Đóng modal khi click vào background
    if (e.target === e.currentTarget) {
      onClose()
    }
  }

  return (
    <div className="login-container" onClick={handleContainerClick}>
      <div className="login-modal">
        <div className="login-form-container">
          <button className="close-button" onClick={handleClose}>×</button>
          <h2>Đăng nhập</h2>
          {error && <div className="error-message">{error}</div>}
          <form onSubmit={handleLogin} className="login-form">
            <div className="form-group">
              <label htmlFor="email">Email:</label>
              <input
                type="email"
                id="email"
                value={credentials.email}
                onChange={(e) => setCredentials({...credentials, email: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="password">Mật khẩu:</label>
              <input
                type="password"
                id="password"
                value={credentials.password}
                onChange={(e) => setCredentials({...credentials, password: e.target.value})}
                required
              />
            </div>
            <button 
              type="submit" 
              className="login-button"
              disabled={isLoading}
            >
              {isLoading ? 'Đang đăng nhập...' : 'Đăng nhập'}
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}

export default Login 