import { Outlet } from 'react-router-dom';
import './AccountantLayout.css';

function AccountantLayout() {
  return (
    <div className="accountant-layout-container">
      <h1 className="accountant-layout-title">Kế toán</h1>
      <Outlet />
    </div>
  );
}

export default AccountantLayout;
