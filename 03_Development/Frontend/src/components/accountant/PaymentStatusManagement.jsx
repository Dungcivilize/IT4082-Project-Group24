import React, { useState, useEffect } from 'react'
import { getAccountantPayments, getAccountantPaymentsByStatus, updatePaymentStatus, updatePaymentDetail, getPaymentPeriods, getApartments } from '../../api/paymentStatus'
import { getApartmentOwnerships } from '../../api/ownership'
import { Modal, Input, Select, InputNumber } from 'antd'
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
  const [editModalVisible, setEditModalVisible] = useState(false)
  const [editAmount, setEditAmount] = useState(null)
  const [editNote, setEditNote] = useState('')

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
      let data = []
      
      if (selectedApartment) {
        // Lấy tất cả ownership của apartment được chọn
        const ownerships = await getApartmentOwnerships(selectedApartment)
        const ownershipIds = ownerships.map(o => o.ownershipId)
        
        // Lấy payments cho từng ownership và gộp lại
        const filters = {
          ...(selectedPeriod && { paymentPeriodId: selectedPeriod }),
          ownershipIds: ownershipIds.join(',') // Chuyển mảng thành chuỗi
        }
        
        data = selectedStatus ? 
          await getAccountantPaymentsByStatus(selectedStatus, filters) :
          await getAccountantPayments(filters)
      } else {
        const filters = {
          ...(selectedPeriod && { paymentPeriodId: selectedPeriod })
        }
        
        data = selectedStatus ? 
          await getAccountantPaymentsByStatus(selectedStatus, filters) :
          await getAccountantPayments(filters)
      }
      
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

  const handlePaymentEdit = async () => {
    try {
      setLoading(true)
      setError('')
      setSuccess('')

      await updatePaymentDetail({
        paymentDetailId: selectedPayment.paymentDetailId,
        amount: editAmount,
        note: editNote
      })

      setSuccess('Cập nhật hóa đơn thành công')
      setEditModalVisible(false)
      setSelectedPayment(null)
      setEditAmount(null)
      setEditNote('')
      loadPayments()
    } catch (error) {
      setError(error.message)
    } finally {
      setLoading(false)
    }
  }

  const showEditModal = (payment) => {
    setSelectedPayment(payment)
    setEditAmount(payment.amount)
    setEditNote(payment.note || '')
    setEditModalVisible(true)
  }

  const handleEditCancel = () => {
    setEditModalVisible(false)
    setSelectedPayment(null)
    setEditAmount(null)
    setEditNote('')
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
            {apartments.map(apartment => (
              <Select.Option 
                key={apartment.apartmentId} 
                value={apartment.apartmentId}
              >
                {apartment.apartmentCode}
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
                <th>Đơn giá</th>
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
                  <td>{formatCurrency(payment.unitPrice)}</td>
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
                    {payment.status === 'UNPAID' && (
                      <div className="action-buttons">
                        <button
                          className="edit-button"
                          onClick={() => showEditModal(payment)}
                          disabled={loading}
                        >
                          Sửa
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

      <Modal
        title={action === 'approve' ? 'Duyệt thanh toán' : 'Từ chối thanh toán'}
        open={isModalVisible}
        onOk={handlePaymentAction}
        onCancel={handleCancel}
        confirmLoading={loading}
      >
        <div>
          <p>Ghi chú:</p>
          <Input.TextArea
            value={note}
            onChange={(e) => setNote(e.target.value)}
            placeholder="Nhập ghi chú (nếu có)"
            rows={4}
          />
        </div>
      </Modal>

      <Modal
        title="Cập nhật hóa đơn"
        open={editModalVisible}
        onOk={handlePaymentEdit}
        onCancel={handleEditCancel}
        confirmLoading={loading}
      >
        <div>
          <p>Số lượng:</p>
          <InputNumber
            value={editAmount}
            onChange={setEditAmount}
            min={0}
            style={{ width: '100%' }}
          />
          <p>Ghi chú:</p>
          <Input.TextArea
            value={editNote}
            onChange={(e) => setEditNote(e.target.value)}
            placeholder="Nhập ghi chú (nếu có)"
            rows={4}
          />
        </div>
      </Modal>
    </div>
  )
}

export default PaymentStatusManagement