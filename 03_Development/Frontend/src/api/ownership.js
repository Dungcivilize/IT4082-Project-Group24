import axios from "axios";

const API_URL = "http://localhost:8080/api";

export const getAllOwnerships = async () => {
  try {
    const response = await axios.get(`${API_URL}/accountant/apartments`);
    return response.data;
  } catch (error) {
    console.error("Error fetching ownerships:", error);
    throw error;
  }
};

export const getApartmentOwnerships = async (apartmentId) => {
  try {
    const response = await axios.get(`${API_URL}/accountant/payment-status/apartments/${apartmentId}/ownerships`);
    return response.data;
  } catch (error) {
    console.error("Error fetching apartment ownerships:", error);
    throw error;
  }
}; 