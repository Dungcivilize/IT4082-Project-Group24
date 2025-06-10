import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './components/Home';
import Login from './components/Login';
import CollectionSummary from './components/manager/CollectionSummary';
import ManagerPayment from './components/manager/ManagerPayment';
import ManagerDashboard from './components/ManagerDashboard';
import ResidentDashboard from './components/citizen/ResidentDashboard';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import ServiceTab from './components/citizen/ServiceTab';
import Layout from './components/admin/Layout';
import UserList from './pages/Users/UserList';
import CreateUser from './pages/Users/CreateUser';
import AdminDashboard from './components/admin/AdminDashboard';
import ManageHousehold from './pages/Residents/ManageHousehold';
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/admin" element={<Layout />}>
          <Route index element={<AdminDashboard />} />
          <Route path="users/list" element={<UserList />} />
          <Route path="users/add" element={<CreateUser />} />
          <Route path="households" element={<ManageHousehold />} />
        </Route>

        
        <Route path="/manager" element={<ManagerDashboard />} />
        <Route path="/dashboard/payments" element={<ManagerPayment />} />
        <Route path="/dashboard/summary" element={<CollectionSummary />} />
        <Route path="/resident" element={<ResidentDashboard />} />
        <Route path="/service" element={<ServiceTab />} />
      </Routes>
    </Router>
  );
}

export default App;
