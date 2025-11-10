package com.example.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.api.model.User;
import com.example.api.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    /**
     * ユーザー名からユーザー情報を読み込み、
     * Spring Security 用の UserDetails オブジェクトに変換して返す。
     *
     * @param username ログイン画面などで入力されたユーザー名
     * @return UserDetails 認証に使用されるユーザーデータ
     * @throws UsernameNotFoundException 指定ユーザーが存在しない場合
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // DB からユーザーを検索（見つからない場合は例外を投げる）
        User user = userRepository.findByUsername(username)
                     .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        /**
         * アプリの User エンティティ を Spring Security の UserDetails に変換
         * - username: ログイン識別子
         * - password: ハッシュ化されたパスワード
         * - roles: ユーザーのロール（ROLE_USER, ROLE_ADMIN など）
         */
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()) 
                .build();
    }
}
