import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './components/Home';
import Login from './components/Login';
import AdminDashboard from './components/AdminDashboard';
import CollectionSummary from './components/manager/CollectionSummary';
import ManagerPayment from './components/manager/ManagerPayment';
import ManagerDashboard from './components/ManagerDashboard';
import ResidentCURD from './components/pages/ResidentCRUD';
import ServiceCURD from './components/pages/ServiceCRUD';
import UserCRUD from './components/pages/UserCRUD';
import ResidentDashboard from './components/citizen/ResidentDashboard';
import ServiceTab from './components/citizen/ServiceTab';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/manager" element={<ManagerDashboard />} />
        <Route path="/dashboard/payments" element={<ManagerPayment />} />
        <Route path="/dashboard/summary" element={<CollectionSummary />} />
        <Route path="/dashboard/users" element={<UserCRUD />} />
        <Route path="/dashboard/presidents" element={<ResidentCURD />} />
        <Route path="/dashboard/services" element={<ServiceCURD />} />
        <Route path="/resident" element={<ResidentDashboard />} />
        <Route path="/service" element={<ServiceTab />} />
      </Routes>
    </Router>
  );
}

export default App;
