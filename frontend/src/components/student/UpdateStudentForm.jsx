import {Form, Formik, useField} from 'formik';
import * as Yup from 'yup';
import {
  Alert,
  AlertIcon,
  Box,
  Button,
  FormLabel,
  Image,
  Input,
  Stack,
  VStack
} from "@chakra-ui/react";
import {
  studentProfilePictureUrl,
  updateStudent,
  uploadStudentProfilePicture
} from "../../services/client.js";
import {
  errorNotification,
  successNotification
} from "../../services/notification.js";
import {useCallback} from "react";
import {useDropzone} from "react-dropzone";

const MyTextInput = ({label, ...props}) => {
  const [field, meta] = useField(props);
  return (
      <Box>
        <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
        <Input className="text-input" {...field} {...props} />
        {meta.touched && meta.error ? (
            <Alert className="error" status={"error"} mt={2}>
              <AlertIcon/>
              {meta.error}
            </Alert>
        ) : null}
      </Box>
  );
};

const MyDropzone = ({studentId, fetchStudents}) => {
  const onDrop = useCallback(acceptedFiles => {
    const formData = new FormData();
    formData.append("file", acceptedFiles[0])

    uploadStudentProfilePicture(
        studentId,
        formData
    ).then(() => {
      successNotification("Uğurlu", "Profil şəkli yükləndi")
      fetchStudents()
    }).catch(() => {
      errorNotification("Səhv", "Profil şəklini yükləmək alınmadı")
    })
  }, [])
  const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

  return (
      <Box {...getRootProps()}
           w={'100%'}
           textAlign={'center'}
           border={'dashed'}
           borderColor={'gray.200'}
           borderRadius={'3xl'}
           p={6}
           rounded={'md'}>
        <input {...getInputProps()} />
        {
          isDragActive ?
              <p>Şəkili bura yerləşdirin ...</p> :
              <p>Şəkili bura sürükləyin və ya şəkil seçmək üçün klikləyin</p>
        }
      </Box>
  )
}

// And now we can use these
const UpdateStudentForm = ({fetchStudents, initialValues, studentId}) => {
  return (
      <>
        <VStack spacing={'5'} mb={'5'}>
          <Image
              borderRadius={'full'}
              boxSize={'150px'}
              objectFit={'cover'}
              src={studentProfilePictureUrl(studentId)}
          />
          <MyDropzone
              studentId={studentId}
              fetchStudents={fetchStudents}
          />
        </VStack>
        <Formik
            initialValues={initialValues}
            validationSchema={Yup.object({
              name: Yup.string()
              .max(15, '15 və ya daha az simvol olmalıdır')
              .required('Tələb olunur'),
              surname: Yup.string()
              .max(20, '20 və ya daha az simvol olmalıdır')
              .required('Tələb olunur'),
              email: Yup.string()
              .email('20 və ya daha az simvol olmalıdır')
              .required('Tələb olunur'),
              group: Yup.string()
              .required('Tələb olunur'),
              course: Yup.number()
              .max(5, "5 və daha az simvol olmalıdır")
              .required('Tələb olunur'),
            })}
            onSubmit={(updatedStudent, {setSubmitting}) => {
              setSubmitting(true);
              updateStudent(studentId, updatedStudent)
              .then(res => {
                console.log(res);
                successNotification(
                    "Tələbə yeniləndi",
                    `${updatedStudent.name} uğurla yeniləndi`
                )
                fetchStudents();
              }).catch(err => {
                console.log(err);
                errorNotification(
                    err.code,
                    err.response.data.message
                )
              }).finally(() => {
                setSubmitting(false);
              })
            }}
        >
          {({isValid, isSubmitting, dirty}) => (
              <Form>
                <Stack spacing={"15px"}>
                  <MyTextInput
                      label="Ad"
                      name="name"
                      type="text"
                      placeholder="Ali"
                  />

                  <MyTextInput
                      label="Soyad"
                      name="surname"
                      type="text"
                      placeholder="Aliyev"
                  />

                  <MyTextInput
                      label="Email adresi"
                      name="email"
                      type="email"
                      placeholder="ali@aztu.edu.az"
                  />

                  <MyTextInput
                      label="Qrup"
                      name="group"
                      type="text"
                      placeholder="662A3"
                  />

                  <MyTextInput
                      label="Kurs"
                      name="course"
                      type="number"
                      placeholder="Kursunuzu daxil edin"
                  />

                  <Button disabled={!(isValid && dirty) || isSubmitting}
                          type="submit">Təsdiq et</Button>
                </Stack>
              </Form>
          )}
        </Formik>
      </>
  );
};

export default UpdateStudentForm;