import { Outlet, NavLink } from 'react-router-dom'

export default function AccountantLayout() {
  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      <nav style={{ width: 200, background: '#eee', padding: 20 }}>
        <h2>Kế toán</h2>
        <ul style={{ listStyle: 'none', padding: 0 }}>
          <li>
            <NavLink 
              to="payment-periods" 
              style={({ isActive }) => ({ fontWeight: isActive ? 'bold' : 'normal' })}
            >
              Đợt thu phí
            </NavLink>
          </li>
        </ul>
      </nav>

      <main style={{ flex: 1, padding: 20 }}>
        <Outlet />
      </main>
    </div>
  )
}
