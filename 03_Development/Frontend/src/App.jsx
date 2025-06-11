import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import Home from './pages/Home'
import ResidentLayout from './components/layouts/ResidentLayout'
import AccountantLayout from './components/layouts/AccountantLayout'
import PersonalInfo from './components/resident/PersonalInfo'
import ResidencyManagement from './components/resident/ResidencyManagement'
import VehicleManagement from './components/resident/VehicleManagement'
import PaymentManagement from './components/resident/PaymentManagement'
import ServiceTypeManagement from './components/accountant/ServiceTypeManagement'
import PaymentPeriodManagement from './components/accountant/PaymentPeriodManagement'
import ServiceUsageManagement from './components/accountant/ServiceUsageManagement'
import PaymentStatusManagement from './components/accountant/PaymentStatusManagement'
import PaymentConfirmation from './components/accountant/PaymentConfirmation'
import FinancialStatistics from './components/accountant/FinancialStatistics'
import Layout from './components/admin/Layout'
import AdminDashboard from './components/admin/AdminDashboard'
import UserList from './pages/Users/UserList'
import CreateUser from './pages/Users/CreateUser'
import ManageHousehold from './pages/Residents/ManageHousehold'

import './App.css'
import ApartmentEmpty from './pages/Residents/ApartmentEmpty'
import StatisticsPage from './pages/Statistics/StatisticsPage'

function App() {
  return (
    <Router>
      <Routes>
        {/* Trang chủ */}
        <Route path="/" element={<Home />} />
        
        {/* Routes cho Cư dân */}
        <Route path="/resident" element={<ResidentLayout />}>
          <Route path="personal-info" element={<PersonalInfo />} />
          <Route path="residency" element={<ResidencyManagement />} />
          <Route path="vehicles" element={<VehicleManagement />} />
          <Route path="payments" element={<PaymentManagement />} />
          {/* Route mặc định cho /resident */}
          <Route index element={<Navigate to="personal-info" replace />} />
        </Route>

        {/* Routes cho Kế toán */}
        <Route path="/accountant" element={<AccountantLayout />}>
          <Route path="personal-info" element={<PersonalInfo />} />
          <Route path="services" element={<ServiceTypeManagement />} />
          <Route path="payment-periods" element={<PaymentPeriodManagement />} />
          <Route path="service-usage" element={<ServiceUsageManagement />} />
          <Route path="payment-status" element={<PaymentStatusManagement />} />
          <Route path="payment-confirmation" element={<PaymentConfirmation />} />
          <Route path="statistics" element={<FinancialStatistics />} />
          {/* Route mặc định cho /accountant */}
          <Route index element={<Navigate to="services" replace />} />
        </Route>

        {/* Routes cho Kế toán */}
        <Route path="/admin" element={<Layout />}>
          <Route path="users/list" element={<UserList />} />
          <Route path="users/add" element={<CreateUser />} />
          <Route path="households" element={<ManageHousehold />} />
          <Route path="apartment-empty" element={<ApartmentEmpty />} />
          <Route path="stats" element={<StatisticsPage />} />
          {/* Route mặc định cho /admin*/}
          <Route index element={<AdminDashboard />} />
        </Route>
      </Routes>
    </Router>
  )
}

export default App
