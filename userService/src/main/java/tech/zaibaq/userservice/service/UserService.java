package tech.zaibaq.userservice.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.zaibaq.userservice.client.ImageServiceClient;
import tech.zaibaq.userservice.exception.InvalidJwtAuthenticationException;
import tech.zaibaq.userservice.model.UserApp;
import tech.zaibaq.userservice.repository.UserRepository;
import tech.zaibaq.userservice.request.UserUpdateRequest;
import tech.zaibaq.userservice.util.JwtTokenUtil;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageServiceClient imageServiceClient;
    private final JwtTokenUtil jwtTokenUtil;

    public UserApp getInformaiton(HttpServletRequest request) throws InvalidJwtAuthenticationException {
        var token = jwtTokenUtil.resolveToken(request);
        var userEmail = jwtTokenUtil.getEmail(token);
        if (jwtTokenUtil.validateToken(token)) {
            var x = userRepository.findByEmail(userEmail).orElseThrow();
            return x;
        }
        return null;
    }

    public UserApp updateUser(HttpServletRequest request, UserUpdateRequest userUpdateRequest) throws InvalidJwtAuthenticationException {
        var token = jwtTokenUtil.resolveToken(request);
        var userEmail = jwtTokenUtil.getEmail(token);
        if (jwtTokenUtil.validateToken(token)) {
            var x = userRepository.findByEmail(userEmail).orElseThrow();
            //prevent user for change Password or email or id
            UserApp u = UserApp.builder().
                            id(x.getId()).
                            firstName((userUpdateRequest.getFirstName()!=null)?userUpdateRequest.getFirstName():x.getFirstName()).
                            lastName((userUpdateRequest.getLastName()!=null)?userUpdateRequest.getLastName():x.getLastName()).
                            createdDate(x.getCreatedDate()).
                            email(x.getEmail()).
                            uname(x.getUname()).
                            password(x.getPassword()).
                            imageUrl(x.getImageUrl()).
                            build();
            return userRepository.save(u);
        }
        throw new InvalidJwtAuthenticationException("invalid JWT");
    }
    public UserApp updateUserImage(HttpServletRequest request, MultipartFile file) throws InvalidJwtAuthenticationException {
        var token = jwtTokenUtil.resolveToken(request);
        var userEmail = jwtTokenUtil.getEmail(token);
        if (jwtTokenUtil.validateToken(token)) {
            var x = userRepository.findByEmail(userEmail).orElseThrow();
            var imageUrl =imageServiceClient.uploadImage(file,"profile-image",x.getId());
            //prevent user for change Password or email or id
            UserApp u = UserApp.builder().
                    id(x.getId()).
                    firstName(x.getFirstName()).
                    lastName(x.getLastName()).
                    createdDate(x.getCreatedDate()).
                    email(x.getEmail()).
                    uname(x.getUname()).
                    password(x.getPassword()).
                    imageUrl(imageUrl).
                    build();
            return userRepository.save(u);
        }
        throw new InvalidJwtAuthenticationException("invalid JWT");
    }

    //TODO :handle user update passwrod
    //TODO :User passwrd update request --> include the currnt Password and new Paasword (create new Function for it).
    public UserApp updateUser(HttpServletRequest request,UserApp user)  {
//        var token=jwtTokenUtil.resolveToken(request);
//        var userEmail = jwtTokenUtil.getEmail(token);
//        //let user  change Password email
//        if (jwtTokenUtil.validateToken(token)) {
//            var x = userRepository.findByEmail(userEmail).orElseThrow();
//            //prevent user for change Password or email or id
//            UserApp u = UserApp.builder().
//                    id(x.getId()).
//                    firstName((userUpdateRequest.getFirstName()!=null)?userUpdateRequest.getFirstName():x.getFirstName()).
//                    lastName((userUpdateRequest.getLastName()!=null)?userUpdateRequest.getLastName():x.getLastName()).
//                    createdDate(x.getCreatedDate()).
//                    email(x.getEmail()).
//                    password(x.getPassword()).
//                    build();
//            return userRepository.save(u);
//        }
//        throw new InvalidJwtAuthenticationException("invalid JWT");
        return userRepository.save(user);
    }

    //TODO :handle not found exception
    public UserApp findUserByID(String id) {
        return userRepository.findById(id).get();
    }

    //TODO :handle not found exception
    public UserApp findUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }
}
