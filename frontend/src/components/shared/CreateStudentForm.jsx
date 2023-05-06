import {Form, Formik, useField} from 'formik';
import * as Yup from 'yup';
import {
  Alert,
  AlertIcon,
  Box,
  Button,
  FormLabel,
  Input,
  Select,
  Stack
} from "@chakra-ui/react";
import {saveStudent} from "../../services/client.js";
import {
  errorNotification,
  successNotification
} from "../../services/notification.js";

const MyTextInput = ({label, ...props}) => {
  // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
  // which we can spread on <input>. We can use field meta to show an error
  // message if the field is invalid and it has been touched (i.e. visited)
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

const MySelect = ({label, ...props}) => {
  const [field, meta] = useField(props);
  return (
      <Box>
        <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
        <Select {...field} {...props} />
        {meta.touched && meta.error ? (
            <Alert className="error" status={"error"} mt={2}>
              <AlertIcon/>
              {meta.error}
            </Alert>
        ) : null}
      </Box>
  );
};

// And now we can use these
const CreateStudentForm = ({onSuccess}) => {
  return (
      <>
        <Formik
            initialValues={{
              name: '',
              surname: '',
              email: '',
              group: '',
              gender: '',
              course: '',
              password: ''
            }}
            validationSchema={Yup.object({
              name: Yup.string()
              .max(15, '15 və ya daha az simvol olmalıdır')
              .required('Tələb olunur'),
              surname: Yup.string()
              .max(15, '15 və ya daha az simvol olmalıdır')
              .required('Tələb olunur'),
              email: Yup.string()
              .email('20 və ya daha az simvol olmalıdır')
              .required('Tələb olunur'),
              group: Yup.string()
              .required('Tələb olunur'),
              password: Yup.string()
              .min(4, '4 və ya daha çox simvol olmalıdır')
              .max(15, '15 və ya daha az simvol olmalıdır')
              .required('Tələb olunur'),
              course: Yup.number()
              .required("Tələb olunur"),
              gender: Yup.string()
              .oneOf(
                  ['MALE', 'FEMALE'],
                  'Yanlış seçim'
              )
              .required('Tələb olunur'),
            })}
            onSubmit={(student, {setSubmitting}) => {
              setSubmitting(true);
              saveStudent(student)
              .then(res => {
                console.log(res);
                successNotification(
                    "Tələbə yadda saxlandı",
                    `${student.name} uğurla yadda saxlandı`
                )
                onSuccess(res.headers["authorization"]);
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
          {({isValid, isSubmitting}) => (
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
                      label="Şifrə"
                      name="password"
                      type="password"
                      placeholder={"təhlükəsiz bir şifrə seçin"}
                  />

                  <MyTextInput
                      label="Kurs"
                      name="course"
                      type="text"
                      placeholder={"kursunuzu seçin"}
                  />

                  <MySelect label="Gender" name="gender">
                    <option value="">Genderi seçin</option>
                    <option value="MALE">Kişi</option>
                    <option value="FEMALE">Qadın</option>
                  </MySelect>

                  <Button disabled={!isValid || isSubmitting} type="submit">
                    Yadda saxla</Button>
                </Stack>
              </Form>
          )}
        </Formik>
      </>
  );
};

export default CreateStudentForm;