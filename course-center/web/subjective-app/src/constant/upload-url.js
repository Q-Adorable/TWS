export const getUploadUrl = (env) => {
  if(env){
    return `https://school.thoughtworks.cn${env}/zuul/program-center/api/uploadImages`
  }
  return  '../api/uploadImages'
}