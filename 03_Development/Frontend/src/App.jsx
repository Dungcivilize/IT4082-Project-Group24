import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import Home from './pages/Home'

import ResidentLayout from './components/layouts/ResidentLayout'
import PersonalInfo from './components/resident/PersonalInfo'
import ResidencyManagement from './components/resident/ResidencyManagement'
import VehicleManagement from './components/resident/VehicleManagement'
import PaymentManagement from './components/resident/PaymentManagement'

import AccountantPage from './components/accountant/AccountantPage' 
import AccountantLayout from './components/accountant/AccountantLayout'
import PaymentPeriods from './components/accountant/PaymentPeriods'
import ProcessingPayments from './components/accountant/ProcessingPayments'

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
          <Route index element={<Navigate to="personal-info" replace />} />
        </Route>

        {/* Routes cho Kế toán */}
        <Route path="/accountant" element={<AccountantLayout />}>
          <Route path="payment-periods" element={<PaymentPeriods />} />
            <Route path="processing-payments" element={<ProcessingPayments />} />
          <Route index element={<Navigate to="payment-periods" replace />} />
        </Route>
      </Routes>
    </Router>
  )
}

export default App
