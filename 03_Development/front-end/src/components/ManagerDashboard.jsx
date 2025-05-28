import React from 'react';
import { useNavigate } from 'react-router-dom';

const ManagerDashboard = () => {
  const navigate = useNavigate();

  // Styles object
  const styles = {
    outerContainer: {
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      minHeight: '100vh',
      width: '100vw',
      margin: 0,
      padding: '20px',
      boxSizing: 'border-box'
    },
    container: {
      width: '100%',
      maxWidth: '800px',
      padding: '2rem',
      backgroundColor: '#fff',
      borderRadius: '12px',
      boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
      textAlign: 'center'
    },
    header: {
      marginBottom: '2.5rem'
    },
    title: {
      fontSize: '3rem',
      color: '#2c3e50',
      marginBottom: '0.5rem',
      fontWeight: '600'
    },
    subtitle: {
      color: '#7f8c8d',
      fontSize: '1rem'
    },
    actions: {
      display: 'grid',
      gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))',
      gap: '1.5rem',
      marginTop: '2rem'
    },
    button: {
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      padding: '1.5rem',
      border: 'none',
      borderRadius: '10px',
      fontSize: '1.5rem',
      fontWeight: '500',
      cursor: 'pointer',
      transition: 'all 0.3s ease',
      minHeight: '120px'
    },
    primaryButton: {
      backgroundColor: '#3498db',
      color: 'white'
    },
    secondaryButton: {
      backgroundColor: '#2ecc71',
      color: 'white'
    }
  };

  // Hover effect
  const handleMouseEnter = (e) => {
    e.currentTarget.style.transform = 'translateY(-3px)';
    e.currentTarget.style.boxShadow = '0 6px 12px rgba(0, 0, 0, 0.1)';
  };

  const handleMouseLeave = (e) => {
    e.currentTarget.style.transform = '';
    e.currentTarget.style.boxShadow = '';
  };

  return (
    <div style={styles.outerContainer}>
      <div style={styles.container}>
        <header style={styles.header}>
          <h2 style={styles.title}>Manager Dashboard</h2>
          <p style={styles.subtitle}>Quản lý hệ thống thu phí</p>
        </header>
        
        <div style={styles.actions}>
          <button 
            style={{...styles.button, ...styles.primaryButton}}
            onClick={() => navigate('/dashboard/payments')}
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
          >
            <i className="fas fa-money-bill-wave" style={{fontSize: '2.5rem', marginBottom: '0.5rem'}}></i>
            <span>Quản lý khoản nộp</span>
          </button>
          
          <button 
            style={{...styles.button, ...styles.secondaryButton}}
            onClick={() => navigate('/dashboard/summary')}
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
          >
            <i className="fas fa-chart-bar" style={{fontSize: '2.5rem', marginBottom: '0.5rem'}}></i>
            <span>Thống kê đợt thu</span>
          </button>
        </div>
      </div>
    </div>
  );
};

export default ManagerDashboard;