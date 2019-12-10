package endorphin.api;

import endorphin.domain.Board;
import endorphin.domain.Post;
import endorphin.domain.Reply;
import endorphin.service.BoardService;
import endorphin.service.PostService;
import endorphin.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/forumapi")
public class ForumApi {
    private final BoardService boardService;
    private final PostService postService;
    private final ReplyService replyService;

    @Autowired
    public ForumApi(BoardService boardService,PostService postService,ReplyService replyService) {
        this.boardService = boardService;
        this.postService = postService;
        this.replyService=replyService;
    }

    /**
     * 显示所有板块文章
     *
     *
     * @return 返回board Id 列表
     */
    @RequestMapping(value = "/listBoards")
    @ResponseBody
    public Map<String, Object> intoBoard() {
        Map<String, Object> map = new HashMap<>();
        List<Board> boards = boardService.listAllBoard();
        if(boards==null){
            map.put("boardId","nofound");
            return map;
        }else{

            for (Board eachboard : boards){
                map.put(eachboard.getBoardId()+"",eachboard.getBoardName());

            };

        return map;
        }
    }

    @RequestMapping(value = "/boardInfo")
    @ResponseBody
    public Map<String, Object> boardInfo(@RequestParam(value = "boardId",required=true)int boardId) {
        Map<String, Object> map = new HashMap<>();
        Board board = boardService.intoBoardByBoardId(boardId);
        if(board==null){
            map.put("boardName","nofound");
            return map;
        }else{
            map.put("name",board.getBoardName());
            map.put("descrip",board.getBoardDesc());
            map.put("postNum",board.getBoardPostNum());
            return map;
        }
    }

    @RequestMapping(value = "/listPosts")
    @ResponseBody
    public Map<String, Object> listPosts(@RequestParam(value = "boardId",required=true)int boardId) {
        Map<String, Object> map = new HashMap<>();
        List<Post> Posts = boardService.listAllPostOfBoard(boardId).getPosts();
        if(Posts==null){
            map.put("PostId","nofound");
            return map;
        }else{
            for (Post eachpost : Posts){
                map.put(""+eachpost.getPostId(),eachpost.getPostTitle());
            };

            return map;
        }
    }
    @RequestMapping(value = "/postInfo")
    @ResponseBody
    public Map<String, Object> postInfo(@RequestParam(value = "postId",required=true)int postId) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> submap = new HashMap<>();
        Post post = postService.listPostContent(postId);
        if(post==null){
            map.put("postName","nofound");
            return map;
        }else{
            map.put("title",post.getPostTitle());
            map.put("content",post.getPostContent());
            map.put("time",post.getPostCreateTime());
           List <Reply> replies=replyService.listReplyByPostId(postId);
//            for (Reply eachreply : replies){
//                submap.put("user",eachreply.getReplyUserName());
//                submap.put("content",eachreply.getReplyContent());
//                submap.put("id",eachreply.getReplyId());
//                submap.put("time",eachreply.getReplyCreateTime());
//                map.put(eachreply.getReplyId()+"",submap);
//                submap.clear();
//            };
            map.put("replies",replies);
            return map;
        }
    }

    @RequestMapping(value = "/addpost")
    @ResponseBody
    public Map<String, Object> addpost(@RequestParam(value = "boardId",required=true)int boardId,@RequestParam(value = "name",required=true)String username,@RequestParam(value = "postTitle",required=true)String postTitle,@RequestParam(value = "content",required=true)String content) {
        Map<String, Object> map = new HashMap<>();
        Post newPost = new Post();
        newPost.setPostBoardId(boardId);
        newPost.setPostReplyCount(0);
        newPost.setPostBadCount(0);
        newPost.setPostContent(content);
        newPost.setPostTitle(postTitle);
        newPost.setPostUserName(username);
        newPost.setPostGoodCount(0);
        newPost.setPostViewCount(0);
        Timestamp createLoginTime = new Timestamp(System.currentTimeMillis());
        newPost.setPostCreateTime(createLoginTime);
        newPost.setPostUpdateTime(createLoginTime);

        postService.addPostByPost(newPost);
        boardService.updatePostNum(newPost.getPostBoardId());
    return map;
    }

    @RequestMapping(value = "/addreply")
    @ResponseBody
    public Map<String, Object> addreply(@RequestParam(value = "postId",required=true)int postId,@RequestParam(value = "name",required=true)String username,@RequestParam(value = "content",required=true)String content) {
        Map<String, Object> map = new HashMap<>();
        Reply newReply = new Reply();
        newReply.setReplyPostId(postId);
        newReply.setReplyBadCount(0);
        newReply.setReplyGoodCount(0);
        newReply.setReplyContent(content);
        newReply.setReplyUserName(username);

        Timestamp createLoginTime = new Timestamp(System.currentTimeMillis());
        newReply.setReplyCreateTime(createLoginTime);
        replyService.addReply(newReply);
        return map;
    }



}
