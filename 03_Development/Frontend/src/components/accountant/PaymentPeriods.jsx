import React, { useEffect, useState } from 'react'
import CreatePaymentPeriod from './CreatePaymentPeriod'
import PaymentDetailsForPeriod from './PaymentDetailsForPeriod'
import './PaymentPeriods.css'  // import css

function PaymentPeriods() {
  const [periods, setPeriods] = useState([])
  const [selectedPeriod, setSelectedPeriod] = useState(null)

  useEffect(() => {
    fetch('http://localhost:8080/api/accountant/payment-periods')
      .then(res => res.json())
      .then(data => setPeriods(data))
      .catch(console.error)
  }, [])

  const handleCreatedPeriod = (newPeriod) => {
    setPeriods(prev => {
      const exists = prev.some(p => p.paymentPeriodId === newPeriod.paymentPeriodId)
      if (exists) return prev
      return [newPeriod, ...prev]
    })
    setSelectedPeriod(newPeriod)
  }

  return (
    <div className="payment-periods-container">
      <h1 className="payment-periods-title">Danh sách đợt thu phí</h1>

      <CreatePaymentPeriod onCreated={handleCreatedPeriod} />

      <ul className="periods-list">
        {periods.map(period => (
          <li key={period.paymentPeriodId} className="periods-list-item">
            <button
              className={`period-button ${selectedPeriod?.paymentPeriodId === period.paymentPeriodId ? 'selected' : ''}`}
              onClick={() => setSelectedPeriod(period)}
            >
              Tháng {period.month} năm {period.year}
            </button>
          </li>
        ))}
      </ul>

      {selectedPeriod && (
        <PaymentDetailsForPeriod paymentPeriodId={selectedPeriod.paymentPeriodId} />
      )}
    </div>
  )
}

export default PaymentPeriods
