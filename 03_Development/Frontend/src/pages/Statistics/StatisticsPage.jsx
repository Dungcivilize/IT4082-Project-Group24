import React, { useState, useEffect, useMemo } from 'react';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid,
  Tooltip, Legend, PieChart, Pie, Cell, ResponsiveContainer
} from 'recharts';
import './StatisticsPage.css';

const COLORS = ['#4f46e5', '#6366f1', '#8b5cf6', '#ec4899', '#f59e0b'];

const StatisticsPage = () => {
  const [residentsStats, setResidentsStats] = useState(null);
  const [vehiclesStats, setVehiclesStats] = useState(null);
  const [apartmentsStats, setApartmentsStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchAllStatistics();
  }, []);

  const fetchAllStatistics = async () => {
    try {
      setLoading(true);
      const [residentsResponse, vehiclesResponse, apartmentsResponse] = await Promise.all([
        fetch('http://localhost:8080/api/statistics/residents'),
        fetch('http://localhost:8080/api/statistics/vehicles'),
        fetch('http://localhost:8080/api/statistics/apartments'),
      ]);

      if (!residentsResponse.ok || !vehiclesResponse.ok || !apartmentsResponse.ok) {
        throw new Error('Failed to fetch statistics');
      }

      setResidentsStats(await residentsResponse.json());
      setVehiclesStats(await vehiclesResponse.json());
      setApartmentsStats(await apartmentsResponse.json());
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const genderData = useMemo(() => {
    if (!residentsStats?.genderStats) return [];
    return Object.entries(residentsStats.genderStats).map(([key, value]) => ({
      name: key === 'male' ? 'Nam' : 'Nữ',
      value
    }));
  }, [residentsStats]);

  const ageData = useMemo(() => {
    if (!residentsStats?.ageStats) return [];
    return Object.entries(residentsStats.ageStats).map(([key, value]) => ({ name: key, value }));
  }, [residentsStats]);

  const statusData = useMemo(() => {
    if (!residentsStats?.statusStats) return [];
    return Object.entries(residentsStats.statusStats).map(([key, value]) => ({
      name: key === 'living' ? 'Đang ở' : 'Tạm vắng',
      value
    }));
  }, [residentsStats]);

  const vehiclesData = useMemo(() => {
    if (!vehiclesStats) return [];
    return Object.entries(vehiclesStats).map(([key, value]) => ({
      name: key === 'motorcycle' ? 'Xe máy' : 'Ô tô',
      value
    }));
  }, [vehiclesStats]);

  const apartmentsData = useMemo(() => {
    if (!apartmentsStats) return [];
    return [
      { name: 'Đang ở', value: apartmentsStats.occupied || 0 },
      { name: 'Trống', value: apartmentsStats.empty || 0 }
    ];
  }, [apartmentsStats]);

  if (loading) return <div className="loading"><div className="spinner" /></div>;

  if (error) {
    return (
      <div className="statistics-page">
        <div className="container">
          <div className="error-box">
            <h2>Lỗi!</h2>
            <p>{error}</p>
            <button onClick={fetchAllStatistics}>Thử lại</button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="statistics-page">
      <div className="container">
        <header className="header">
          <h1>Thống kê tổng quan</h1>
          <p>Xem thống kê dân cư, căn hộ và phương tiện</p>
        </header>

        <section>
          <h2 className="section-title">Thống kê dân cư</h2>
          <div className="grid grid-3">
            <div className="chart-card">
              <h3>Giới tính</h3>
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie data={genderData} dataKey="value" cx="50%" cy="50%" outerRadius={80}
                       label={({ name, percent }) => `${name} (${(percent * 100).toFixed(0)}%)`}>
                    {genderData.map((entry, i) => <Cell key={i} fill={COLORS[i % COLORS.length]} />)}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </div>

            <div className="chart-card">
              <h3>Độ tuổi</h3>
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={ageData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Bar dataKey="value" fill="#4f46e5" />
                </BarChart>
              </ResponsiveContainer>
            </div>

            <div className="chart-card">
              <h3>Trạng thái</h3>
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie data={statusData} dataKey="value" cx="50%" cy="50%" outerRadius={80}
                       label={({ name, percent }) => `${name} (${(percent * 100).toFixed(0)}%)`}>
                    {statusData.map((entry, i) => <Cell key={i} fill={COLORS[i % COLORS.length]} />)}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </div>
          </div>
        </section>

        <section>
          <h2 className="section-title">Thống kê căn hộ</h2>
          <div className="grid grid-2">
            <div className="chart-card">
              <h3>Tình trạng căn hộ</h3>
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie data={apartmentsData} dataKey="value" cx="50%" cy="50%" outerRadius={100}
                       label={({ name, value, percent }) => `${name}: ${value} (${(percent * 100).toFixed(0)}%)`}>
                    {apartmentsData.map((entry, i) => <Cell key={i} fill={COLORS[i % COLORS.length]} />)}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </div>

            <div className="chart-card">
              <h3>Biểu đồ cột căn hộ</h3>
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={apartmentsData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="value" fill="#8b5cf6" />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>
        </section>

        <section>
          <h2 className="section-title">Thống kê phương tiện</h2>
          <div className="grid grid-2">
            <div className="chart-card">
              <h3>Phân bố phương tiện</h3>
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie data={vehiclesData} dataKey="value" cx="50%" cy="50%" outerRadius={100}
                       label={({ name, value, percent }) => `${name}: ${value} (${(percent * 100).toFixed(0)}%)`}>
                    {vehiclesData.map((entry, i) => <Cell key={i} fill={COLORS[i % COLORS.length]} />)}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </div>

            <div className="chart-card">
              <h3>Biểu đồ cột phương tiện</h3>
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={vehiclesData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="value" fill="#ec4899" />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>
        </section>

        <div className="summary-grid">
          <div className="summary-card blue">
            <h4>Tổng dân cư</h4>
            <p>{residentsStats ? Object.values(residentsStats.genderStats || {}).reduce((a, b) => a + b, 0) : 0}</p>
          </div>
          <div className="summary-card green">
            <h4>Tổng căn hộ</h4>
            <p>{apartmentsStats ? apartmentsStats.occupied + apartmentsStats.empty : 0}</p>
          </div>
          <div className="summary-card purple">
            <h4>Tổng phương tiện</h4>
            <p>{vehiclesStats ? Object.values(vehiclesStats).reduce((a, b) => a + b, 0) : 0}</p>
          </div>
        </div>

        <div className="refresh-container">
          <button className="refresh-button" onClick={fetchAllStatistics}>Làm mới dữ liệu</button>
        </div>
      </div>
    </div>
  );
};

export default StatisticsPage;
