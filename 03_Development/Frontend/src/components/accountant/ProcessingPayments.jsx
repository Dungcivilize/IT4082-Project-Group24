import React, { useEffect, useState } from 'react'
import './ProcessingPayments.css'

function ProcessingPayments() {
  const [payments, setPayments] = useState([])

  useEffect(() => {
    fetch('http://localhost:8080/api/accountant/payments/processing')
      .then(res => res.json())
      .then(data => setPayments(data))
      .catch(err => console.error('Lỗi khi load payment processing:', err))
  }, [])

  function handleApprove(paymentId) {
    fetch(`http://localhost:8080/api/accountant/payments/${paymentId}/approve`, {
      method: 'POST'
    })
      .then(res => {
        if (res.ok) {
          setPayments(prev => prev.filter(p => p.paymentId !== paymentId))
        } else {
          return res.text().then(msg => alert(`Lỗi duyệt: ${msg}`))
        }
      })
  }

  function handleReject(paymentId) {
    const note = prompt('Nhập lý do từ chối:')
    if (!note) return

    fetch(`http://localhost:8080/api/accountant/payments/${paymentId}/reject`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ note })
    })
      .then(res => {
        if (res.ok) {
          setPayments(prev => prev.filter(p => p.paymentId !== paymentId))
        } else {
          return res.text().then(msg => alert(`Lỗi từ chối: ${msg}`))
        }
      })
  }

  return (
    <div className="processing-payments-container">
      <h2 className="processing-payments-title">Danh sách thanh toán đang xử lý</h2>
      {payments.length === 0 ? (
        <p className="processing-payments-empty">Không có thanh toán nào đang xử lý</p>
      ) : (
        <table className="processing-payments-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Người gửi</th>
              <th>Số tiền</th>
              <th>Tên hóa đơn</th>
              <th>Loại hóa đơn</th>
              <th>Ghi chú</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {payments.map(p => (
              <tr key={p.paymentId}>
                <td>{p.paymentId}</td>
                <td>{p.paymentDetail?.apartment?.apartmentCode || 'N/A'}</td>
                <td>{p.price}</td>
                <td>{p.paymentDetail?.serviceType?.serviceName || 'N/A'}</td>
                <td>{p.paymentDetail?.serviceType?.serviceType || 'N/A'}</td>
                <td>{p.note}</td>
                <td>
                  <button
                    className="approve-button"
                    onClick={() => handleApprove(p.paymentId)}
                  >
                    Duyệt
                  </button>
                  <button
                    className="reject-button"
                    onClick={() => handleReject(p.paymentId)}
                  >
                    Từ chối
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

export default ProcessingPayments
