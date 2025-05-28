import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../../assets/css/Dashboard.module.css';

const ManagerPayment = () => {
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem('user'));

  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);

  // Form state
  const initialFormState = {
    paymentId: '',
    cpName: '',
    collectionDate: '',
    cost: '',
    citizenId: '',
    status: '',
  };

  const [formData, setFormData] = useState(initialFormState);
  const [isEditing, setIsEditing] = useState(false);

  // Load payment list
  const fetchPayments = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch('http://localhost:8080/api/payments');
      if (!response.ok) throw new Error('Failed to fetch payments');
      const data = await response.json();
      setPayments(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPayments();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('user');
    navigate('/login');
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const resetForm = () => {
    setFormData(initialFormState);
    setIsEditing(false);
    setSuccessMessage(null);
  };

  const showSuccess = (message) => {
    setSuccessMessage(message);
    setTimeout(() => setSuccessMessage(null), 3000);
  };

  // Add new payment
  const handleAddPayment = async (e) => {
    e.preventDefault();
    
    if (!formData.cpName || !formData.collectionDate || !formData.cost || !formData.citizenId || !formData.status) {
      setError('Please fill in all required fields');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/payments', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          cpName: formData.cpName,
          collectionDate: formData.collectionDate,
          cost: Number(formData.cost),
          citizenId: formData.citizenId,
          status: formData.status,
        }),
      });

      if (!response.ok) throw new Error('Failed to add payment');
      
      const newPayment = await response.json();
      setPayments(prev => [...prev, newPayment]);
      resetForm();
      showSuccess('Payment added successfully!');
    } catch (err) {
      setError(err.message);
    }
  };

  // Edit payment
  const handleEditClick = (payment) => {
    setFormData({
      paymentId: payment.paymentId,
      cpName: payment.cpName,
      collectionDate: payment.collectionDate,
      cost: payment.cost,
      citizenId: payment.citizenId,
      status: payment.status || '',
    });
    setIsEditing(true);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  // Update payment
  const handleUpdatePayment = async (e) => {
    e.preventDefault();

    if (!formData.status) {
      setError('Please select a status');
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/api/payments/${formData.paymentId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          cpName: formData.cpName,
          collectionDate: formData.collectionDate,
          cost: Number(formData.cost),
          citizenId: formData.citizenId,
          status: formData.status,
        }),
      });

      if (!response.ok) throw new Error('Failed to update payment');
      
      const updatedPayment = await response.json();
      setPayments(prev =>
        prev.map(p => (p.paymentId === updatedPayment.paymentId ? updatedPayment : p))
      );
      resetForm();
      showSuccess('Payment updated successfully!');
    } catch (err) {
      setError(err.message);
    }
  };

  // Delete payment
  const handleDeletePayment = async (paymentId) => {
    if (!window.confirm('Are you sure you want to delete this payment?')) return;

    try {
      const response = await fetch(`http://localhost:8080/api/payments/${paymentId}`, {
        method: 'DELETE',
      });

      if (!response.ok) throw new Error('Failed to delete payment');
      
      setPayments(prev => prev.filter(p => p.paymentId !== paymentId));
      showSuccess('Payment deleted successfully!');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className={styles.dashboard}>
      <nav className={`${styles.navbar} navbar-expand-lg`}>
        <div className="container-fluid">
          <div className="d-flex justify-content-center align-items-center w-100"> {/* Đổi từ justify-content-between thành justify-content-center */}
            <h1 className={`${styles.title} mb-0 text-center`}>Payment Management</h1> {/* Thêm text-center */}
          </div>
        </div>
      </nav>

      <div className="container py-4">
        {/* User Info Card */}
        <div className={`${styles.card} card mb-4 shadow-sm`}>
          <div className="card-body">
            <div>
              <div>
                <h5 className="card-title mb-3">User Information</h5>
                <div className="row">
                  <div className="col-md-6">
                    <p className="mb-2"><strong>Name:</strong> {user?.fullname}</p>
                    <p className="mb-2"><strong>Email:</strong> {user?.username}</p>
                  </div>
                  <div className="col-md-6">
                    <p className="mb-2"><strong>Role:</strong> Manager</p>
                    <p className="mb-0"><strong>ID:</strong> {user?.userId}</p>
                  </div>
                </div>
              </div>
              <div className="text-end">
                <span className="badge bg-primary p-2">Active</span>
              </div>
            </div>
          </div>
        </div>

        {/* Messages */}
        {error && (
          <div className="alert alert-danger alert-dismissible fade show" role="alert">
            {error}
            <button 
              type="button" 
              className="btn-close" 
              onClick={() => setError(null)}
              aria-label="Close"
            ></button>
          </div>
        )}
        
        {successMessage && (
          <div className="alert alert-success alert-dismissible fade show" role="alert">
            {successMessage}
            <button 
              type="button" 
              className="btn-close" 
              onClick={() => setSuccessMessage(null)}
              aria-label="Close"
            ></button>
          </div>
        )}

        {/* Payment Form */}
        <div className={`${styles.card} card mb-4 shadow-sm`}>
          <div className="card-body">
            <h5 className="card-title mb-4">
              {isEditing ? 'Update Payment' : 'Add New Payment'}
            </h5>
            
            <form onSubmit={isEditing ? handleUpdatePayment : handleAddPayment}>
              <div className="row g-3">
                <div className="col-md-6">
                  <label className="form-label">Collection Name*</label>
                  <input
                    type="text"
                    className="form-control"
                    name="cpName"
                    value={formData.cpName}
                    onChange={handleChange}
                    required
                  />
                </div>
                
                <div className="col-md-6">
                  <label className="form-label">Collection Date*</label>
                  <input
                    type="date"
                    className="form-control"
                    name="collectionDate"
                    value={formData.collectionDate}
                    onChange={handleChange}
                    required
                  />
                </div>
                
                <div className="col-md-6">
                  <label className="form-label">Amount*</label>
                  <input
                    type="number"
                    className="form-control"
                    name="cost"
                    value={formData.cost}
                    onChange={handleChange}
                    required
                    min="0"
                    step="25000"
                  />
                </div>
                
                <div className="col-md-6">
                  <label className="form-label">Citizen ID*</label>
                  <input
                    type="text"
                    className="form-control"
                    name="citizenId"
                    value={formData.citizenId}
                    onChange={handleChange}
                    required
                  />
                </div>
                
                <div className="col-md-6">
                  <label className="form-label">Status*</label>
                  <select
                    className="form-select"
                    name="status"
                    value={formData.status}
                    onChange={handleChange}
                    required
                  >
                    <option value="">-- Select Status --</option>
                    <option value="Paid">Paid</option>
                    <option value="Unpaid">Unpaid</option>
                    <option value="Pending">Pending</option>
                  </select>
                </div>
                
                <div className="col-12 mt-2">
                  <div className="d-flex justify-content-start gap-2">
                    <button type="submit" className="btn btn-primary px-4">
                      {isEditing ? 'Update' : 'Add Payment'}
                    </button>
                    {isEditing && (
                      <button 
                        type="button" 
                        className="btn btn-outline-secondary"
                        onClick={resetForm}
                      >
                        Cancel
                      </button>
                    )}
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>

        {/* Payments List */}
        <div className={`${styles.card} card shadow-sm`}>
          <div className="card-body">
            <div className="d-flex justify-content-between align-items-center mb-4">
              <h5 className="card-title mb-0">Payment Records</h5>
              <div>
                <span className="badge bg-light text-dark">
                  Total: {payments.length}
                </span>
              </div>
            </div>
            
            {loading ? (
              <div className="text-center py-4">
                <div className="spinner-border text-primary" role="status">
                  <span className="visually-hidden">Loading...</span>
                </div>
                <p className="mt-2">Loading payments...</p>
              </div>
            ) : error ? (
              <div className="alert alert-danger">{error}</div>
            ) : payments.length === 0 ? (
              <div className="alert alert-info">No payments found.</div>
            ) : (
              <div className="table-responsive">
                <table className="table table-hover align-middle">
                  <thead className="table-light">
                    <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Date</th>
                      <th>Amount</th>
                      <th>Citizen ID</th>
                      <th>Status</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {payments.map(payment => (
                      <tr key={payment.paymentId}>
                        <td>{payment.paymentId}</td>
                        <td>{payment.cpName}</td>
                        <td>{new Date(payment.collectionDate).toLocaleDateString()}</td>
                        <td>{payment.cost.toFixed(2)}</td>
                        <td>{payment.citizenId}</td>
                        <td>
                          <span className={`badge ${
                            payment.status === 'Paid' ? 'bg-success' : 
                            payment.status === 'Unpaid' ? 'bg-danger' : 'bg-warning'
                          }`}>
                            {payment.status}
                          </span>
                        </td>
                        <td>
                          <div className="d-flex gap-2">
                            <button
                              className="btn btn-sm btn-outline-primary"
                              onClick={() => handleEditClick(payment)}
                            >
                              <i className="bi bi-pencil"></i> Edit
                            </button>
                            <button
                              className="btn btn-sm btn-outline-danger"
                              onClick={() => handleDeletePayment(payment.paymentId)}
                            >
                              <i className="bi bi-trash"></i> Delete
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManagerPayment;