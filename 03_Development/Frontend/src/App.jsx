import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import Home from './pages/Home'
import ResidentLayout from './components/layouts/ResidentLayout'
import PersonalInfo from './components/resident/PersonalInfo'
import ResidencyManagement from './components/resident/ResidencyManagement'
import VehicleManagement from './components/resident/VehicleManagement'
import PaymentManagement from './components/resident/PaymentManagement'
import AccountantLayout from './components/accountant/AccountantLayout'
import PaymentPeriods from './components/accountant/PaymentPeriods'
import PaymentPeriodDetail from './components/accountant/PaymentPeriodDetail'
import CreatePaymentPeriod from './components/accountant/CreatePaymentPeriod'  
import EditPaymentPeriod from './components/accountant/EditPaymentPeriod'
import './App.css'

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

        {/* Routes cho kế toán */}
        <Route path="/accountant" element={<AccountantLayout />}>
          <Route path="payment-periods" element={<PaymentPeriods />} />
          <Route path="payment-periods/:id" element={<PaymentPeriodDetail />} />
          <Route path="payment-periods/create" element={<CreatePaymentPeriod />} />
           <Route path="payment-periods/:id/edit" element={<EditPaymentPeriod />} />
          <Route index element={<Navigate to="payment-periods" replace />} />
        </Route>
      </Routes>
    </Router>
  )
}

export default App
