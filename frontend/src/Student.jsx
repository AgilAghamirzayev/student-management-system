import {
    Wrap,
    WrapItem,
    Spinner,
    Text
} from '@chakra-ui/react';
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import { useEffect, useState } from 'react';
import { getStudents } from "./services/client.js";
import CardWithImage from "./components/student/StudentCard.jsx";
import CreateStudentDrawer from "./components/student/CreateStudentDrawer.jsx";
import {errorNotification} from "./services/notification.js";

const Student = () => {

    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setError] = useState("");

    const fetchStudents = () => {
        setLoading(true);
        getStudents().then(res => {
            setStudents(res.data)
        }).catch(err => {
            setError(err.response.data.message)
            errorNotification(
                err.code,
                err.response.data.message
            )
        }).finally(() => {
            setLoading(false)
        })
    }

    useEffect(() => {
        fetchStudents();
    }, [])

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }

    if (err) {
        return (
            <SidebarWithHeader>
                <CreateStudentDrawer
                    fetchStudents={fetchStudents}
                />
                <Text mt={5}>Ooops xəta baş verdi</Text>
            </SidebarWithHeader>
        )
    }

    if(students.length <= 0) {
        return (
            <SidebarWithHeader>
                <CreateStudentDrawer
                    fetchStudents={fetchStudents}
                />
                <Text mt={5}>Tələbə mövcud deyil</Text>
            </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <CreateStudentDrawer
                fetchStudents={fetchStudents}
            />
            <Wrap justify={"center"} spacing={"30px"}>
                {students.map((student, index) => (
                    <WrapItem key={index}>
                        <CardWithImage
                            {...student}
                            imageNumber={index}
                            fetchStudents={fetchStudents}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default Student;