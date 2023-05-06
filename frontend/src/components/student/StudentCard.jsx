import {
  AlertDialog,
  AlertDialogBody,
  AlertDialogContent,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogOverlay,
  Avatar,
  Box,
  Button,
  Center,
  Flex,
  Heading,
  Image,
  Stack,
  Tag,
  Text,
  useColorModeValue,
  useDisclosure,
} from '@chakra-ui/react';

import {useRef} from 'react'
import {
  deleteStudent,
  studentProfilePictureUrl
} from "../../services/client.js";
import {
  errorNotification,
  successNotification
} from "../../services/notification.js";
import UpdateStudentDrawer from "./UpdateStudentDrawer.jsx";

export default function CardWithImage({
  id,
  name,
  surname,
  email,
  group,
  course,
  gender,
  imageNumber,
  fetchStudents
}) {
  const userGender = gender === "MALE" ? "Kişi" : "Qadın";

  const {isOpen, onOpen, onClose} = useDisclosure()
  const cancelRef = useRef()

  return (
      <Center py={6}>
        <Box
            maxW={'300px'}
            minW={'300px'}
            w={'full'}
            m={2}
            bg={useColorModeValue('gainsboro', 'gray.800')}
            boxShadow={'lg'}
            rounded={'md'}
            overflow={'hidden'}>
          <Image
              h={'120px'}
              w={'full'}
              src={
                'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
              }
              objectFit={'cover'}
          />
          <Flex justify={'center'} mt={-12}>
            <Avatar
                size={'xl'}
                src={studentProfilePictureUrl(id)}
                alt={'Author'}
                css={{
                  border: '2px solid white',
                }}
            />
          </Flex>

          <Box p={6}>
            <Stack spacing={2} align={'center'} mb={5}>
              <Tag borderRadius={"full"}>{id}</Tag>
              <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                {name} {surname}
              </Heading>
              <Text color={'gray.500'}>{email}</Text>

              <Text color={'gray.500'}>Kurs: {course} |
                Qrup: {group} | {userGender}</Text>
            </Stack>
          </Box>
          <Stack direction={'row'} justify={'center'} spacing={6} p={4}>
            <Stack>
              <UpdateStudentDrawer
                  initialValues={{name, surname, email, group, course}}
                  studentId={id}
                  fetchStudents={fetchStudents}
              />
            </Stack>
            <Stack>
              <Button
                  bg={'red.400'}
                  color={'white'}
                  rounded={'full'}
                  _hover={{
                    transform: 'translateY(-2px)',
                    boxShadow: 'lg'
                  }}
                  _focus={{
                    bg: 'green.500'
                  }}
                  onClick={onOpen}
              >  Sil
              </Button>
              <AlertDialog
                  isOpen={isOpen}
                  leastDestructiveRef={cancelRef}
                  onClose={onClose}
              >
                <AlertDialogOverlay>
                  <AlertDialogContent>
                    <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                      Tələbəni sil
                    </AlertDialogHeader>

                    <AlertDialogBody>
                      {name}-i silmək istədiyinizə əminsiniz? Daha sonra bu
                      əməliyyatı geri qaytara bilməzsiniz.
                    </AlertDialogBody>

                    <AlertDialogFooter>
                      <Button ref={cancelRef} onClick={onClose}>
                        Ləğv et
                      </Button>
                      <Button colorScheme='red' onClick={() => {
                        deleteStudent(id).then(res => {
                          console.log(res)
                          successNotification(
                              'Tələbə silindi',
                              `${name} uğurla silindi`
                          )
                          fetchStudents();

                        }).catch(err => {
                          console.log(err);
                          errorNotification(
                              err.code,
                              err.response.data.message
                          )
                        }).finally(() => {
                          onClose()
                        })
                      }} ml={3}>
                        Sil
                      </Button>
                    </AlertDialogFooter>
                  </AlertDialogContent>
                </AlertDialogOverlay>
              </AlertDialog>
            </Stack>

          </Stack>
        </Box>
      </Center>
  );
}