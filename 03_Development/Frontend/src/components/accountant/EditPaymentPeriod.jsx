import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'

export default function EditPaymentPeriod() {
  const { id } = useParams()
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetch(`http://localhost:8080/api/accountant/payment-periods/${id}`)
      .then(res => res.json())
      .then(json => {
        setData(json)
        setLoading(false)
      })
  }, [id])

  const handleInputChange = (apartmentId, paymentDetailId, value) => {
    setData(prev => {
      const updated = { ...prev }
      const apartment = updated.apartments.find(a => a.apartmentId === apartmentId)
      const detail = apartment.paymentDetails.find(d => d.paymentDetailId === paymentDetailId)

      const unitPrice = detail.unitPrice || 0
      const amount = parseFloat(value || 0) * unitPrice

      detail.amount = amount
      detail.volume = value // Lưu tạm volume (lưu lượng) để FE dùng

      return updated
    })
  }

  const handleSave = (paymentDetailId, amount) => {
    fetch(`http://localhost:8080/api/accountant/payment-details/${paymentDetailId}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ amount }),
    })
  }

  if (loading) return <p>Đang tải dữ liệu...</p>
  if (!data) return <p>Không tìm thấy đợt thu</p>

  return (
    <div>
      <h2>Kỳ {data.month}/{data.year} - {data.note}</h2>
      {data.apartments.map(ap => (
        <div key={ap.apartmentId} style={{ border: '1px solid #ccc', padding: 10, marginBottom: 20 }}>
          <h4>Căn hộ: {ap.apartmentCode}</h4>
          {ap.paymentDetails.map(detail => (
            <div key={detail.paymentDetailId} style={{ marginBottom: 10 }}>
              <b>{detail.serviceName}</b> ({detail.serviceType})<br />

              {['electricity', 'water'].includes(detail.serviceType) && (
                <input
                  type="number"
                  placeholder="Nhập lưu lượng"
                  value={detail.volume || ''}
                  onChange={e =>
                    handleInputChange(ap.apartmentId, detail.paymentDetailId, e.target.value)
                  }
                />
              )}

              {['motorbike', 'car'].includes(detail.serviceType) && (
                <p>Số lượng xe: {detail.amount / detail.unitPrice}</p>
              )}

              {detail.serviceType === 'management' && <p>Phí quản lý: {detail.amount}đ</p>}

              <p>→ Số tiền: {detail.amount?.toFixed(2)}đ</p>

              {['electricity', 'water'].includes(detail.serviceType) && (
                <button onClick={() => handleSave(detail.paymentDetailId, detail.amount)}>Lưu hóa đơn</button>
              )}
            </div>
          ))}
        </div>
      ))}
    </div>
  )
}
