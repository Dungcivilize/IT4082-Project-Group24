import React, { useState, useEffect } from 'react'
import axios from 'axios'
import { API_URL } from '../../constants/api'
import '../../styles/Resident.css'
import QRCodeImage from '../../assets/QRCode.jpg'

const PaymentManagement = () => {
  const [activeTab, setActiveTab] = useState('pending')
  const [pendingPayments, setPendingPayments] = useState([])
  const [paymentHistory, setPaymentHistory] = useState([])
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [selectedPayment, setSelectedPayment] = useState(null)
  const [transactionCode, setTransactionCode] = useState('')

  useEffect(() => {
    loadPendingPayments()
    loadPaymentHistory()
  }, [])

  const loadPendingPayments = async () => {
    try {
      const user = JSON.parse(localStorage.getItem('user'))
      if (!user?.userId) {
        setError('Vui lòng đăng nhập để sử dụng tính năng này')
        return
      }

      const response = await axios.get(`${API_URL}/residents/payments/pending?userId=${user.userId}`)
      // Lọc ra các khoản phí chưa thanh toán hoặc đang xử lý
      const pendingPayments = response.data.filter(payment => 
        payment.paymentStatus === 'UNPAID' || payment.paymentStatus === 'PROCESSING'
      )
      setPendingPayments(pendingPayments)
      setError('')
    } catch (error) {
      console.error('Lỗi khi tải danh sách phí chờ thanh toán:', error)
      setError(error.response?.data || 'Có lỗi xảy ra khi tải danh sách phí chờ thanh toán')
    }
  }

  const loadPaymentHistory = async () => {
    try {
      const user = JSON.parse(localStorage.getItem('user'))
      if (!user?.userId) return

      const response = await axios.get(`${API_URL}/residents/payments/history?userId=${user.userId}`)
      // Chỉ lấy các khoản phí đã thanh toán thành công
      const paidPayments = response.data.filter(payment => payment.paymentStatus === 'PAID')
      setPaymentHistory(paidPayments)
    } catch (error) {
      console.error('Lỗi khi tải lịch sử thanh toán:', error)
      setError(error.response?.data || 'Có lỗi xảy ra khi tải lịch sử thanh toán')
    }
  }

  const handleSubmitPayment = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')

    if (!selectedPayment) {
      setError('Vui lòng chọn khoản phí cần thanh toán')
      return
    }

    if (!transactionCode.trim()) {
      setError('Vui lòng nhập mã giao dịch')
      return
    }

    try {
      const user = JSON.parse(localStorage.getItem('user'))
      if (!user?.userId) {
        setError('Vui lòng đăng nhập để sử dụng tính năng này')
        return
      }

      await axios.post(`${API_URL}/residents/payments/submit?userId=${user.userId}`, {
        paymentDetailId: selectedPayment.paymentDetailId,
        transactionCode: transactionCode.trim()
      })

      setSuccess('Gửi yêu cầu thanh toán thành công')
      setSelectedPayment(null)
      setTransactionCode('')
      loadPendingPayments()
      loadPaymentHistory()
    } catch (error) {
      setError(error.response?.data || 'Có lỗi xảy ra khi gửi yêu cầu thanh toán')
    }
  }

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount)
  }

  const formatDateTime = (dateTimeStr) => {
    const date = new Date(dateTimeStr)
    return date.toLocaleString('vi-VN')
  }

  const getStatusText = (status) => {
    switch (status?.toUpperCase()) {
      case 'PAID':
        return 'Đã thanh toán'
      case 'PROCESSING':
        return 'Đang xử lý'
      case 'UNPAID':
        return 'Chưa thanh toán'
      default:
        return status || 'Không xác định'
    }
  }

  const getStatusClass = (status) => {
    switch (status?.toUpperCase()) {
      case 'PAID':
        return 'status-success'
      case 'PROCESSING':
        return 'status-warning'
      case 'UNPAID':
        return 'status-danger'
      default:
        return ''
    }
  }

  const getServiceTypeText = (type) => {
    const serviceTypes = {
      'water': 'Dịch vụ nước',
      'electricity': 'Dịch vụ điện',
      'maintenance': 'Dịch vụ bảo trì',
      'motorbike': 'Dịch vụ xe máy',
      'car': 'Dịch vụ xe hơi',
      'management': 'Dịch vụ quản lý'
    }
    return serviceTypes[type] || type
  }

  const renderPaymentCard = (payment) => (
    <div key={payment.paymentDetailId} className="payment-card">
      <div className="payment-header">
        <h3>{payment.serviceName}</h3>
        <span className={`status-badge ${getStatusClass(payment.paymentStatus)}`}>
          {getStatusText(payment.paymentStatus)}
        </span>
      </div>
      <div className="payment-info">
        <p>Loại dịch vụ: {getServiceTypeText(payment.serviceType)}</p>
        <p>Kỳ thu: {payment.periodInfo}</p>
        <p>Số lượng: {payment.amount}</p>
        <p>Đơn giá: {formatCurrency(payment.unitPrice)}</p>
        <p className="total-price">Tổng tiền: {formatCurrency(payment.totalPrice)}</p>
        <p>Ngày tạo: {formatDateTime(payment.createdAt)}</p>
        {payment.transactionCode && (
          <p>Mã giao dịch: {payment.transactionCode}</p>
        )}
        {payment.note && (
          <p>Ghi chú: {payment.note}</p>
        )}
      </div>
      {payment.paymentStatus === 'UNPAID' && (
        <button
          className="pay-button"
          onClick={() => {
            setSelectedPayment(payment)
            setTransactionCode('')
          }}
        >
          Thanh toán
        </button>
      )}
    </div>
  )

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
            <p>Tổng tiền: {formatCurrency(selectedPayment.totalPrice)}</p>
            <div className="qr-code-container">
              <img src={QRCodeImage} alt="Mã QR thanh toán" className="qr-code-image" />
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
                    setSelectedPayment(null)
                    setTransactionCode('')
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
                {pendingPayments.map(payment => renderPaymentCard(payment))}
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
                {paymentHistory.map(payment => renderPaymentCard(payment))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  )
}

export default PaymentManagement 