import request from "superagent";

export const addConfig = async (path, message, token, configFileContent) => {
  const url = `http://localhost:5000/config/${path}?comment=${message}`;
  await request
    .post(url)
    .set('Content-Type', 'text/plain')
    .set('Authorization', `Bearer ${token}`)
    .send(configFileContent)
};

export const deleteConfig = async (path, token) => {
  const url = `http://localhost:5000/config/${path}?comment=delete file ${path}`;
  await request
    .delete(url)
    .set('Content-Type', 'text/plain')
    .set('Authorization', `Bearer ${token}`)
    .send();
};
