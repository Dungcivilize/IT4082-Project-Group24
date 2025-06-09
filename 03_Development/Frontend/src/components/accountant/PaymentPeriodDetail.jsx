import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'

export default function PaymentPeriodDetail() {
  const { id } = useParams()
  const [period, setPeriod] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetch(`http://localhost:8080/api/accountant/payment-periods/${id}`)
      .then(res => res.json())
      .then(data => {
        setPeriod(data)
        setLoading(false)
      })
      .catch(() => setLoading(false))
  }, [id])

  if (loading) return <p>Đang tải chi tiết đợt thu phí...</p>
  if (!period) return <p>Không tìm thấy đợt thu phí</p>

  return (
    <div>
      <h1>Chi tiết đợt thu phí tháng {period.month}/{period.year}</h1>
      <p>Trạng thái: {period.status}</p>
      <p>Ghi chú: {period.note}</p>
      <Link to="/accountant/payment-periods">&lt; Quay lại danh sách</Link>

      <h2>Danh sách căn hộ</h2>
      {period.apartments && period.apartments.length > 0 ? (
        period.apartments.map(apartment => (
          <div key={apartment.apartmentId} style={{ border: '1px solid #ccc', marginBottom: 20, padding: 10 }}>
            <h3>Căn hộ: {apartment.apartmentCode}</h3>
            {apartment.paymentDetails.length > 0 ? (
              <ul style={{ paddingLeft: 20 }}>
                {apartment.paymentDetails.map(pd => (
                  <li key={pd.paymentDetailId} style={{ marginBottom: 5 }}>
                    <b>{pd.serviceName}</b> ({pd.serviceType}) - 
                    Số tiền: {pd.amount} - 
                    Trạng thái: {pd.status} - 
                    Ngày tạo: {new Date(pd.createdAt).toLocaleString()}
                  </li>
                ))}
              </ul>
            ) : (
              <p>Không có hóa đơn cho căn hộ này.</p>
            )}
          </div>
        ))
      ) : (
        <p>Không có căn hộ hoặc hóa đơn nào trong đợt thu này.</p>
      )}
    </div>
  )
}
