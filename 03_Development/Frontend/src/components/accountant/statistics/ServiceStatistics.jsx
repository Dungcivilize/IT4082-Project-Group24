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
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { getServiceTypes, getStatisticsByService } from '../../../api/statistics';

function ServiceStatistics() {
  const [services, setServices] = useState([]);
  const [selectedService, setSelectedService] = useState('');
  const [statistics, setStatistics] = useState(null);

  useEffect(() => {
    loadServiceTypes();
  }, []);

  useEffect(() => {
    if (selectedService) {
      loadStatistics();
    }
  }, [selectedService]);

  const loadServiceTypes = async () => {
    try {
      const data = await getServiceTypes();
      setServices(data);
      if (data.length > 0) {
        setSelectedService(data[0].serviceTypeId);
      }
    } catch (error) {
      console.error('Error loading service types:', error);
    }
  };

  const loadStatistics = async () => {
    try {
      const data = await getStatisticsByService(selectedService);
      setStatistics(data);
    } catch (error) {
      console.error('Error loading statistics:', error);
    }
  };

  const handleServiceChange = (event) => {
    setSelectedService(event.target.value);
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
  };

  if (!statistics) {
    return <Typography>Đang tải dữ liệu...</Typography>;
  }

  const chartData = statistics.periodDetails.map(period => ({
    name: `${period.month}/${period.year}`,
    amount: period.totalAmount
  }));

  return (
    <div>
      <FormControl fullWidth sx={{ mb: 3 }}>
        <InputLabel>Loại dịch vụ</InputLabel>
        <Select
          value={selectedService}
          label="Loại dịch vụ"
          onChange={handleServiceChange}
        >
          {services.map((service) => (
            <MenuItem key={service.serviceTypeId} value={service.serviceTypeId}>
              {service.serviceName}
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
              Biểu đồ theo kỳ thu phí
            </Typography>
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={chartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="amount" stroke="#8884d8" name="Số tiền" />
              </LineChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>

        <Grid item xs={12}>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Kỳ thu phí</TableCell>
                  <TableCell align="right">Số tiền</TableCell>
                  <TableCell align="right">Đã thu</TableCell>
                  <TableCell align="right">Chưa thu</TableCell>
                  <TableCell align="right">Tỷ lệ thu (%)</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {statistics.periodDetails.map((period) => (
                  <TableRow key={period.paymentPeriodId}>
                    <TableCell>{`Tháng ${period.month}/${period.year}`}</TableCell>
                    <TableCell align="right">{formatCurrency(period.totalAmount)}</TableCell>
                    <TableCell align="right">{formatCurrency(period.collectedAmount)}</TableCell>
                    <TableCell align="right">{formatCurrency(period.uncollectedAmount)}</TableCell>
                    <TableCell align="right">
                      {((period.collectedAmount / period.totalAmount) * 100).toFixed(1)}%
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

export default ServiceStatistics; 