import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import Home from './pages/Home'
import ResidentLayout from './components/layouts/ResidentLayout'
import PersonalInfo from './components/resident/PersonalInfo'
import ResidencyManagement from './components/resident/ResidencyManagement'
import VehicleManagement from './components/resident/VehicleManagement'
import PaymentManagement from './components/resident/PaymentManagement'
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
      </Routes>
    </Router>
  )
}

export default App
