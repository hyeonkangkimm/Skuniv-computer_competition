package com.example.skunivProject.domain.chat.service;

import com.example.skunivProject.domain.chat.dto.ResponseDto;
import com.example.skunivProject.domain.chat.entity.ChatRoom;
import com.example.skunivProject.domain.chat.exception.ChatException;
import com.example.skunivProject.domain.chat.exception.code.ChatErrorCode;
import com.example.skunivProject.domain.chat.repository.ChatRoomRepository;
import com.example.skunivProject.domain.team.entity.Team;
import com.example.skunivProject.domain.team.repository.TeamRepository;
import com.example.skunivProject.domain.teammember.entity.TeamMember;
import com.example.skunivProject.domain.teammember.enums.Role;
import com.example.skunivProject.domain.teammember.repository.TeamMemberRepository;
import com.example.skunivProject.domain.users.entity.Users;
import com.example.skunivProject.domain.users.exception.UserException;
import com.example.skunivProject.domain.users.exception.code.UserErrorCode;
import com.example.skunivProject.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public ChatRoom createChatRoom(Long teamId, String username) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.TEAM_NOT_FOUND));

        boolean isLeader = team.getMembers().stream()
                .anyMatch(member -> member.getRole() == Role.LEADER && member.getUser().getId().equals(currentUser.getId()));
        if (!isLeader) {
            throw new ChatException(ChatErrorCode.FORBIDDEN_TO_CREATE_CHATROOM);
        }

        chatRoomRepository.findByTeamId(teamId).ifPresent(room -> {
            throw new ChatException(ChatErrorCode.CHATROOM_ALREADY_EXISTS);
        });

        String roomName = team.getRecruitPost().getTitle() + " 팀 채팅방";
        ChatRoom newChatRoom = ChatRoom.builder()
                .teamId(teamId)
                .name(roomName)
                .build();

        return chatRoomRepository.save(newChatRoom);
    }

    @Transactional(readOnly = true)
    public List<ResponseDto.ChatRoomInfo> getMyChatRooms(String username) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        List<TeamMember> myTeamMemberships = teamMemberRepository.findAllByUser(currentUser);

        return myTeamMemberships.stream()
                .map(TeamMember::getTeam)
                .distinct()
                .map(team -> chatRoomRepository.findByTeamId(team.getId())
                        .map(chatRoom -> ResponseDto.ChatRoomInfo.builder()
                                .roomId(chatRoom.getId())
                                .roomName(chatRoom.getName())
                                .memberCount(team.getMembers().size())
                                .build())
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
