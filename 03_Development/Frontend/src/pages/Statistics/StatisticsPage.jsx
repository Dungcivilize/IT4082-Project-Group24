import React, { useState } from 'react';
import { Tabs, Tab, Box, Typography, Container } from '@mui/material';
import PeriodStatistics from './PeriodStatistics';
import ServiceStatistics from './ServiceStatistics';

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`statistics-tabpanel-${index}`}
      aria-labelledby={`statistics-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 3 }}>
          {children}
        </Box>
      )}
    </div>
  );
}

function StatisticsPage() {
  const [value, setValue] = useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Thống kê tài chính
      </Typography>
      
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={value} onChange={handleChange} aria-label="statistics tabs">
          <Tab label="Thống kê theo kỳ thu phí" />
          <Tab label="Thống kê theo loại dịch vụ" />
        </Tabs>
      </Box>

      <TabPanel value={value} index={0}>
        <PeriodStatistics />
      </TabPanel>
      <TabPanel value={value} index={1}>
        <ServiceStatistics />
      </TabPanel>
    </Container>
  );
}

export default StatisticsPage; 