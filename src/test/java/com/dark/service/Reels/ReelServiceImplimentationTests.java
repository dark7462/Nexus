package com.dark.service.Reels;

import com.dark.Exceptions.UserException;
import com.dark.model.Reels;
import com.dark.model.User;
import com.dark.repository.ReelRepository;
import com.dark.service.Users.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReelServiceImplimentationTests {

    @Mock
    private ReelRepository reelRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReelServiceImplimentation reelService;

    @Test
    void createReelShouldAssignUserAndSave() {
        Reels reel = new Reels();
        User user = new User();

        when(reelRepository.save(any(Reels.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reels saved = reelService.createReel(reel, user);

        assertEquals(user, saved.getUser());
    }

    @Test
    void findAllReelsShouldDelegateToRepository() {
        when(reelRepository.findAll()).thenReturn(List.of(new Reels(), new Reels()));
        assertEquals(2, reelService.findAllReels().size());
    }

    @Test
    void findUserReelsShouldThrowWhenUserMissing() {
        when(userService.findById(2)).thenReturn(null);
        assertThrows(UserException.class, () -> reelService.findUserReels(2));
    }
}
