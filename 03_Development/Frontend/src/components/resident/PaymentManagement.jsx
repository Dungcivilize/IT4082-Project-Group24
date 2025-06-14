import React, { useState, useEffect } from 'react';
import {
  getResidentPayments,
  getResidentPaymentsByStatus,
} from '../../api/paymentStatus';
import { API_URL } from '../../constants/api';
import '../../styles/Resident.css';
import QRCodeImage from '../../assets/QRCode.jpg';
import { TableCell, Chip } from '@mui/material';
import axios from 'axios';

const PaymentManagement = () => {
  const [activeTab, setActiveTab] = useState('pending');
  const [pendingPayments, setPendingPayments] = useState([]);
  const [paymentHistory, setPaymentHistory] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [selectedPayment, setSelectedPayment] = useState(null);
  const [transactionCode, setTransactionCode] = useState('');

  useEffect(() => {
    loadPendingPayments();
    loadPaymentHistory();
  }, []);

  const loadPendingPayments = async () => {
    try {
      const user = JSON.parse(localStorage.getItem('user'));
      if (!user?.userId) {
        setError('Vui lòng đăng nhập để sử dụng tính năng này');
        return;
      }

      const ownershipId = user.ownershipId;
      if (!ownershipId) {
        setError('Không tìm thấy thông tin quyền sở hữu');
        return;
      }

      const payments = await getResidentPayments(ownershipId);
      // Lọc ra các khoản phí chưa thanh toán hoặc đang xử lý
      const pendingPayments = payments.filter(
        (payment) =>
          payment.status === 'UNPAID' || payment.status === 'PROCESSING'
      );
      setPendingPayments(pendingPayments);
      setError('');
    } catch (error) {
      console.error('Lỗi khi tải danh sách phí chờ thanh toán:', error);
      setError(
        error.response?.data ||
          'Có lỗi xảy ra khi tải danh sách phí chờ thanh toán'
      );
    }
  };

  const loadPaymentHistory = async () => {
    try {
      const user = JSON.parse(localStorage.getItem('user'));
      if (!user?.userId) return;

      const ownershipId = user.ownershipId;
      if (!ownershipId) {
        setError('Không tìm thấy thông tin quyền sở hữu');
        return;
      }

      const payments = await getResidentPaymentsByStatus(ownershipId, 'PAID');
      setPaymentHistory(payments);
    } catch (error) {
      console.error('Lỗi khi tải lịch sử thanh toán:', error);
      setError(
        error.response?.data || 'Có lỗi xảy ra khi tải lịch sử thanh toán'
      );
    }
  };

  const handleSubmitPayment = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (!selectedPayment) {
      setError('Vui lòng chọn khoản phí cần thanh toán');
      return;
    }

    if (!transactionCode.trim()) {
      setError('Vui lòng nhập mã giao dịch');
      return;
    }

    try {
      const user = JSON.parse(localStorage.getItem('user'));
      if (!user?.userId) {
        setError('Vui lòng đăng nhập để sử dụng tính năng này');
        return;
      }

      await axios.post(
        `${API_URL}/residents/payments/submit?userId=${user.userId}`,
        {
          paymentDetailId: selectedPayment.paymentDetailId,
          transactionCode: transactionCode.trim(),
        }
      );

      setSuccess('Gửi yêu cầu thanh toán thành công');
      setSelectedPayment(null);
      setTransactionCode('');
      loadPendingPayments();
      loadPaymentHistory();
    } catch (error) {
      setError(
        error.response?.data || 'Có lỗi xảy ra khi gửi yêu cầu thanh toán'
      );
    }
  };

  const formatNumber = (value) => {
    return value ? value.toLocaleString('vi-VN') : '0';
  };

  const formatCurrency = (value) => {
    return value ? `${value.toLocaleString('vi-VN')} VND` : '0 VND';
  };

  const formatDateTime = (dateTimeStr) => {
    const date = new Date(dateTimeStr);
    return date.toLocaleString('vi-VN');
  };

  const getStatusText = (status) => {
    switch (status?.toUpperCase()) {
      case 'UNPAID':
        return 'Chưa thanh toán';
      case 'PROCESSING':
        return 'Đang xử lý';
      case 'PAID':
        return 'Đã thanh toán';
      default:
        return 'Không xác định';
    }
  };

  const getStatusClass = (status) => {
    switch (status?.toUpperCase()) {
      case 'PAID':
        return 'status-success';
      case 'PROCESSING':
        return 'status-warning';
      case 'UNPAID':
        return 'status-danger';
      default:
        return '';
    }
  };

  const getServiceTypeText = (type) => {
    const serviceTypes = {
      Nước: 'Dịch vụ nước',
      Điện: 'Dịch vụ điện',
    };
    return serviceTypes[type] || type;
  };

  const renderPaymentCard = (payment) => (
    <div key={payment.paymentDetailId} className="payment-card">
      <div className="payment-header">
        <h3>{payment.serviceName}</h3>
        <span className={`status-badge ${getStatusClass(payment.status)}`}>
          {getStatusText(payment.status)}
        </span>
      </div>
      <div className="payment-info">
        <p>Loại dịch vụ: {payment.serviceTypeName}</p>
        <p>Kỳ thu: {payment.periodInfo}</p>
        <p>Số lượng: {formatNumber(payment.amount)}</p>
        <p>Đơn giá: {formatCurrency(payment.unitPrice)}</p>
        <p className="total-price">
          Tổng tiền: {formatCurrency(payment.price)}
        </p>
        <p>Ngày tạo: {formatDateTime(payment.createdAt)}</p>
        {payment.transactionCode && (
          <p>Mã giao dịch: {payment.transactionCode}</p>
        )}
        {payment.paidAt && (
          <p>Ngày thanh toán: {formatDateTime(payment.paidAt)}</p>
        )}
        {payment.note && payment.status === 'UNPAID' && (
          <p className="payment-note warning">
            <strong>Lưu ý:</strong> {payment.note}
          </p>
        )}
      </div>
      {payment.status === 'UNPAID' && (
        <button
          className="pay-button"
          onClick={() => {
            setSelectedPayment(payment);
            setTransactionCode('');
          }}
        >
          Thanh toán
        </button>
      )}
    </div>
  );

  const getStatusLabel = (status) => {
    switch (status?.toUpperCase()) {
      case 'UNPAID':
        return 'Chưa thanh toán';
      case 'PROCESSING':
        return 'Đang xử lý';
      case 'PAID':
        return 'Đã thanh toán';
      default:
        return 'Không xác định';
    }
  };

  const getStatusColor = (status) => {
    switch (status?.toUpperCase()) {
      case 'UNPAID':
        return 'error';
      case 'PROCESSING':
        return 'warning';
      case 'PAID':
        return 'success';
      default:
        return 'default';
    }
  };

  const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('vi-VN');
  };

  return (
    <div className="payment-management">
      <h2>Quản lý thanh toán</h2>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}

      {selectedPayment && (
        <div className="payment-form-overlay">
          <div className="payment-form">
            <h3>Thanh toán phí {selectedPayment.serviceName}</h3>
            <p>Kỳ thu: {selectedPayment.periodInfo}</p>
            <p>Tổng tiền: {formatCurrency(selectedPayment.price)}</p>
            <div className="qr-code-container">
              <img
                src={QRCodeImage}
                alt="Mã QR thanh toán"
                className="qr-code-image"
              />
              <p className="qr-code-note">Quét mã QR để thanh toán</p>
            </div>
            <form onSubmit={handleSubmitPayment}>
              <div className="form-group">
                <label>Mã giao dịch:</label>
                <input
                  type="text"
                  value={transactionCode}
                  onChange={(e) => setTransactionCode(e.target.value)}
                  placeholder="Nhập mã giao dịch sau khi chuyển khoản"
                  required
                />
              </div>
              <div className="button-group">
                <button type="submit" className="submit-button">
                  Xác nhận thanh toán
                </button>
                <button
                  type="button"
                  className="cancel-button"
                  onClick={() => {
                    setSelectedPayment(null);
                    setTransactionCode('');
                  }}
                >
                  Hủy
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="tabs">
        <button
          className={`tab ${activeTab === 'pending' ? 'active' : ''}`}
          onClick={() => setActiveTab('pending')}
        >
          Chờ thanh toán
        </button>
        <button
          className={`tab ${activeTab === 'history' ? 'active' : ''}`}
          onClick={() => setActiveTab('history')}
        >
          Lịch sử thanh toán
        </button>
      </div>

      <div className="tab-content">
        {activeTab === 'pending' && (
          <div className="payment-list">
            {pendingPayments.length === 0 ? (
              <p>Không có khoản phí nào chờ thanh toán</p>
            ) : (
              <div className="payment-grid">
                {pendingPayments.map((payment) => renderPaymentCard(payment))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'history' && (
          <div className="payment-list">
            {paymentHistory.length === 0 ? (
              <p>Chưa có lịch sử thanh toán</p>
            ) : (
              <div className="payment-grid">
                {paymentHistory.map((payment) => (
                  <div key={payment.paymentDetailId} className="payment-card">
                    <div className="payment-header">
                      <h3>{payment.serviceName}</h3>
                      <span
                        className={`status-badge ${getStatusClass(
                          payment.status
                        )}`}
                      >
                        {getStatusText(payment.status)}
                      </span>
                    </div>
                    <div className="payment-info">
                      <p>Loại dịch vụ: {payment.serviceTypeName}</p>
                      <p>Kỳ thu: {payment.periodInfo}</p>
                      <p>Số lượng: {formatNumber(payment.amount)}</p>
                      <p>Đơn giá: {formatCurrency(payment.unitPrice)}</p>
                      <p className="total-price">
                        Tổng tiền: {formatCurrency(payment.price)}
                      </p>
                      <p>Ngày tạo: {formatDate(payment.createdAt)}</p>
                      {payment.transactionCode && (
                        <p>Mã giao dịch: {payment.transactionCode}</p>
                      )}
                      {payment.paidAt && (
                        <p>Ngày thanh toán: {formatDate(payment.paidAt)}</p>
                      )}
                      {payment.note && (
                        <p className="payment-note warning">
                          <strong>Lưu ý:</strong> {payment.note}
                        </p>
                      )}
                    </div>
                    {payment.status === 'UNPAID' && (
                      <button
                        className="pay-button"
                        onClick={() => {
                          setSelectedPayment(payment);
                          setTransactionCode('');
                        }}
                      >
                        Thanh toán
                      </button>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default PaymentManagement;
