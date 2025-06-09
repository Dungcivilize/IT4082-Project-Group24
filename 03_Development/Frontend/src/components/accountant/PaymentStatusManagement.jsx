import React, { useState, useEffect } from 'react'
import { getAllPayments, getPaymentsByStatus, updatePaymentStatus, getPaymentPeriods, getApartments } from '../../api/paymentStatus'
import { Modal, Input, Select } from 'antd'
import '../../styles/PaymentStatus.css'

const { Option } = Select

const PaymentStatusManagement = () => {
  const [payments, setPayments] = useState([])
  const [selectedStatus, setSelectedStatus] = useState('PROCESSING')
  const [selectedPeriod, setSelectedPeriod] = useState(null)
  const [selectedApartment, setSelectedApartment] = useState(null)
  const [periods, setPeriods] = useState([])
  const [apartments, setApartments] = useState([])
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [loading, setLoading] = useState(false)
  const [selectedPayment, setSelectedPayment] = useState(null)
  const [isModalVisible, setIsModalVisible] = useState(false)
  const [note, setNote] = useState('')
  const [action, setAction] = useState('')

  useEffect(() => {
    loadPayments()
  }, [selectedStatus, selectedPeriod, selectedApartment])

  useEffect(() => {
    loadFilterOptions()
  }, [])

  const loadFilterOptions = async () => {
    try {
      const [periodsData, apartmentsData] = await Promise.all([
        getPaymentPeriods(),
        getApartments()
      ])
      setPeriods(periodsData)
      setApartments(apartmentsData)
    } catch (error) {
      setError('Không thể tải dữ liệu bộ lọc: ' + error.message)
    }
  }

  const loadPayments = async () => {
    try {
      setLoading(true)
      setError('')
      const filters = {
        ...(selectedPeriod && { paymentPeriodId: selectedPeriod }),
        ...(selectedApartment && { ownershipId: selectedApartment })
      }
      
      const data = selectedStatus ? 
        await getPaymentsByStatus(selectedStatus, filters) :
        await getAllPayments(filters)
      setPayments(data)
    } catch (error) {
      setError(error.message)
    } finally {
      setLoading(false)
    }
  }

  const handlePaymentAction = async () => {
    try {
      setLoading(true)
      setError('')
      setSuccess('')

      await updatePaymentStatus({
        paymentDetailId: selectedPayment.paymentDetailId,
        status: action === 'approve' ? 'PAID' : 'UNPAID',
        note: note
      })

      setSuccess(action === 'approve' ? 'Duyệt thanh toán thành công' : 'Đã từ chối thanh toán')
      setIsModalVisible(false)
      setSelectedPayment(null)
      setNote('')
      loadPayments()
    } catch (error) {
      setError(error.message)
    } finally {
      setLoading(false)
    }
  }

  const showPaymentModal = (payment, actionType) => {
    setSelectedPayment(payment)
    setAction(actionType)
    setIsModalVisible(true)
  }

  const handleCancel = () => {
    setIsModalVisible(false)
    setSelectedPayment(null)
    setNote('')
  }

  const formatDate = (dateString) => {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleString('vi-VN');
  }

  const formatCurrency = (amount) => {
    return amount ? `${amount.toLocaleString('vi-VN')} VND` : '0 VND';
  }

  const formatNumber = (value) => {
    return value ? value.toLocaleString('vi-VN') : '0';
  }

  return (
    <div className="payment-status-container">
      <h2>Theo dõi trạng thái thanh toán</h2>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}

      <div className="filter-section">
        <div className="filter-group">
          <label>Kỳ thu phí:</label>
          <Select
            allowClear
            placeholder="Chọn kỳ thu phí"
            value={selectedPeriod}
            onChange={setSelectedPeriod}
            style={{ width: 200 }}
          >
            {periods.map(period => (
              <Select.Option key={period.paymentPeriodId} value={period.paymentPeriodId}>
                {`Tháng ${period.month}/${period.year}`}
              </Select.Option>
            ))}
          </Select>
        </div>

        <div className="filter-group">
          <label>Căn hộ:</label>
          <Select
            allowClear
            placeholder="Chọn căn hộ"
            value={selectedApartment}
            onChange={setSelectedApartment}
            style={{ width: 200 }}
          >
            {apartments.map(ownership => (
              <Select.Option key={ownership.ownershipId} value={ownership.ownershipId}>
                {ownership.apartmentCode}
              </Select.Option>
            ))}
          </Select>
        </div>

        <div className="filter-group">
          <label>Trạng thái:</label>
          <Select
            allowClear
            placeholder="Chọn trạng thái"
            value={selectedStatus}
            onChange={setSelectedStatus}
            style={{ width: 200 }}
          >
            <Select.Option value="UNPAID">Chưa thanh toán</Select.Option>
            <Select.Option value="PROCESSING">Đang xử lý</Select.Option>
            <Select.Option value="PAID">Đã thanh toán</Select.Option>
          </Select>
        </div>
      </div>

      <div className="payments-list">
        {loading ? (
          <div className="loading">Đang tải dữ liệu...</div>
        ) : payments.length === 0 ? (
          <div className="no-data">Không có dữ liệu</div>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Mã căn hộ</th>
                <th>Chủ sở hữu</th>
                <th>Kỳ thu phí</th>
                <th>Dịch vụ</th>
                <th>Số lượng</th>
                <th>Thành tiền</th>
                <th>Trạng thái</th>
                <th>Mã giao dịch</th>
                <th>Thời gian tạo</th>
                <th>Thời gian thanh toán</th>
                <th>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              {payments.map(payment => (
                <tr key={payment.paymentDetailId}>
                  <td>{payment.apartmentCode}</td>
                  <td>{payment.ownerName}</td>
                  <td>{payment.periodInfo}</td>
                  <td>{payment.serviceTypeName}</td>
                  <td>{formatNumber(payment.amount)}</td>
                  <td>{formatCurrency(payment.price)}</td>
                  <td>
                    <span className={`status ${payment.status?.toLowerCase()}`}>
                      {payment.status === 'UNPAID' ? 'Chưa thanh toán' :
                       payment.status === 'PROCESSING' ? 'Đang xử lý' :
                       payment.status === 'PAID' ? 'Đã thanh toán' : ''}
                    </span>
                  </td>
                  <td>{payment.transactionCode || '-'}</td>
                  <td>{formatDate(payment.createdAt)}</td>
                  <td>{formatDate(payment.paidAt)}</td>
                  <td>
                    {payment.status === 'PROCESSING' && (
                      <div className="action-buttons">
                        <button 
                          className="approve-button"
                          onClick={() => showPaymentModal(payment, 'approve')}
                          disabled={loading}
                        >
                          Duyệt
                        </button>
                        <button 
                          className="reject-button"
                          onClick={() => showPaymentModal(payment, 'reject')}
                          disabled={loading}
                        >
                          Từ chối
                        </button>
                      </div>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {isModalVisible && (
        <Modal
          visible={isModalVisible}
          onCancel={handleCancel}
          onOk={handlePaymentAction}
          title={action === 'approve' ? "Duyệt thanh toán" : "Từ chối thanh toán"}
          okText={action === 'approve' ? "Duyệt" : "Từ chối"}
          cancelText="Hủy"
        >
          <div className="payment-detail-info">
            <p><strong>Mã căn hộ:</strong> {selectedPayment?.apartmentCode}</p>
            <p><strong>Chủ sở hữu:</strong> {selectedPayment?.ownerName}</p>
            <p><strong>Kỳ thu phí:</strong> {selectedPayment?.periodInfo}</p>
            <p><strong>Dịch vụ:</strong> {selectedPayment?.serviceTypeName}</p>
            <p><strong>Số lượng:</strong> {formatNumber(selectedPayment?.amount)}</p>
            <p><strong>Đơn giá:</strong> {formatCurrency(selectedPayment?.unitPrice)}</p>
            <p><strong>Tổng tiền:</strong> {formatCurrency(selectedPayment?.price)}</p>
            {selectedPayment?.transactionCode && (
              <p><strong>Mã giao dịch:</strong> {selectedPayment.transactionCode}</p>
            )}
          </div>
          <div className="note-input">
            <label>Ghi chú:</label>
            <Input.TextArea
              value={note}
              onChange={(e) => setNote(e.target.value)}
              placeholder={action === 'approve' ? "Nhập ghi chú nếu cần" : "Nhập lý do từ chối"}
              rows={4}
            />
          </div>
        </Modal>
      )}
    </div>
  )
}

export default PaymentStatusManagement