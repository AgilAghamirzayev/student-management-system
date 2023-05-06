import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {Box, Text} from "@chakra-ui/react";

const Home = () => {

  return (
      <SidebarWithHeader>
          <Box py={12} px={6}>
            <Box>
              <Text fontSize={"4xl"} fontWeight={"bold"} mb={8}>
                <span style={{color: "#FF6363"}}>İ</span>
                <span style={{color: "#E46363"}}>d</span>
                <span style={{color: "#C86363"}}>a</span>
                <span style={{color: "#A86363"}}>r</span>
                <span style={{color: "#8F6363"}}>ə</span>
                <span style={{color: "#776363"}}>etmə</span>
                <span style={{color: "#5F6363"}}> Paneli</span>
              </Text>
            </Box>
          </Box>
      </SidebarWithHeader>
)
}

export default Home;