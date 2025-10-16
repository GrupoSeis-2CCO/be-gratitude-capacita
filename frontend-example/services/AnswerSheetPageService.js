import { api } from "../api.js";


export async function getAnswerSheetData(userId, examId) {
  if (!userId || !examId) throw new Error("Parâmetros obrigatórios ausentes");
  const resp = await api.get(`/answersheet/${examId}/${userId}`);
  return resp.data;
}

const AnswerSheetPageService = {
  getAnswerSheetData
};

export default AnswerSheetPageService;
