import axiosInstance from './axiosInstance';

export const getPaymentPeriods = async () => {
  const response = await axiosInstance.get('/api/payment-periods');
  return response.data;
};

export const getServiceTypes = async () => {
  const response = await axiosInstance.get('/api/service-types');
  return response.data;
};

export const getStatisticsByPeriod = async (periodId) => {
  const response = await axiosInstance.get(`/api/statistics/by-period/${periodId}`);
  return response.data;
};

export const getStatisticsByService = async (serviceTypeId) => {
  const response = await axiosInstance.get(`/api/statistics/by-service/${serviceTypeId}`);
  return response.data;
}; 