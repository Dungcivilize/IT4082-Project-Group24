import axios from "axios";

const API_URL = "http://localhost:8080/api";

export const getAllPaymentPeriods = async () => {
  try {
    const response = await axios.get(`${API_URL}/payment-periods`);
    return response.data;
  } catch (error) {
    console.error("Error fetching payment periods:", error);
    throw error;
  }
};

export const createPaymentPeriod = async (data) => {
  try {
    // Chuyển đổi dữ liệu sang số nguyên
    const payload = {
      month: parseInt(data.month),
      year: parseInt(data.year),
      note: data.note
    };
    console.log("Sending payload:", payload); // Log để debug

    const response = await axios.post(`${API_URL}/payment-periods`, payload);
    return response.data;
  } catch (error) {
    console.error("Error creating payment period:", error.response?.data || error.message);
    throw error;
  }
}; 