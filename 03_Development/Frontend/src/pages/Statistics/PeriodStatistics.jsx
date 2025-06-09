import React, { useState, useEffect } from 'react';
import { 
  Paper, 
  Table, 
  TableBody, 
  TableCell, 
  TableContainer, 
  TableHead, 
  TableRow,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Typography,
  Box,
  Grid
} from '@mui/material';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { getPaymentPeriods, getStatisticsByPeriod } from '../../api/statistics';

function PeriodStatistics() {
  const [periods, setPeriods] = useState([]);
  const [selectedPeriod, setSelectedPeriod] = useState('');
  const [statistics, setStatistics] = useState(null);

  useEffect(() => {
    loadPaymentPeriods();
  }, []);

  useEffect(() => {
    if (selectedPeriod) {
      loadStatistics();
    }
  }, [selectedPeriod]);

  const loadPaymentPeriods = async () => {
    try {
      const data = await getPaymentPeriods();
      setPeriods(data);
      if (data.length > 0) {
        setSelectedPeriod(data[0].paymentPeriodId);
      }
    } catch (error) {
      console.error('Error loading payment periods:', error);
    }
  };

  const loadStatistics = async () => {
    try {
      const data = await getStatisticsByPeriod(selectedPeriod);
      setStatistics(data);
    } catch (error) {
      console.error('Error loading statistics:', error);
    }
  };

  const handlePeriodChange = (event) => {
    setSelectedPeriod(event.target.value);
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
  };

  if (!statistics) {
    return <Typography>Đang tải dữ liệu...</Typography>;
  }

  const chartData = statistics.serviceDetails.map(service => ({
    name: service.serviceName,
    amount: service.totalAmount
  }));

  return (
    <div>
      <FormControl fullWidth sx={{ mb: 3 }}>
        <InputLabel>Kỳ thu phí</InputLabel>
        <Select
          value={selectedPeriod}
          label="Kỳ thu phí"
          onChange={handlePeriodChange}
        >
          {periods.map((period) => (
            <MenuItem key={period.paymentPeriodId} value={period.paymentPeriodId}>
              {`Tháng ${period.month}/${period.year}`}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2, mb: 3 }}>
            <Typography variant="h6" gutterBottom>
              Tổng quan
            </Typography>
            <Box sx={{ mt: 2 }}>
              <Typography>
                Tổng số tiền: {formatCurrency(statistics.totalAmount)}
              </Typography>
              <Typography>
                Đã thu: {formatCurrency(statistics.collectedAmount)}
              </Typography>
              <Typography>
                Chưa thu: {formatCurrency(statistics.uncollectedAmount)}
              </Typography>
              <Typography>
                Tỷ lệ thu: {((statistics.collectedAmount / statistics.totalAmount) * 100).toFixed(1)}%
              </Typography>
            </Box>
          </Paper>
        </Grid>

        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2, height: 300 }}>
            <Typography variant="h6" gutterBottom>
              Biểu đồ theo loại dịch vụ
            </Typography>
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={chartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="amount" fill="#8884d8" name="Số tiền" />
              </BarChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>

        <Grid item xs={12}>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Loại dịch vụ</TableCell>
                  <TableCell align="right">Số tiền</TableCell>
                  <TableCell align="right">Đã thu</TableCell>
                  <TableCell align="right">Chưa thu</TableCell>
                  <TableCell align="right">Tỷ lệ thu (%)</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {statistics.serviceDetails.map((service) => (
                  <TableRow key={service.serviceTypeId}>
                    <TableCell>{service.serviceName}</TableCell>
                    <TableCell align="right">{formatCurrency(service.totalAmount)}</TableCell>
                    <TableCell align="right">{formatCurrency(service.collectedAmount)}</TableCell>
                    <TableCell align="right">{formatCurrency(service.uncollectedAmount)}</TableCell>
                    <TableCell align="right">
                      {((service.collectedAmount / service.totalAmount) * 100).toFixed(1)}%
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </Grid>
      </Grid>
    </div>
  );
}

export default PeriodStatistics; 