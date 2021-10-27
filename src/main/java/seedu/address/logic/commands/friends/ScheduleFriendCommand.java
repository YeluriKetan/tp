package seedu.address.logic.commands.friends;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_DAY_TIME_FORMAT;

import java.util.Map;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.CommandType;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.friend.Friend;
import seedu.address.model.friend.FriendId;
import seedu.address.model.friend.FriendName;
import seedu.address.model.friend.Schedule;
import seedu.address.model.friend.exceptions.InvalidDayTimeException;
import seedu.address.model.game.GameId;
import seedu.address.model.gamefriendlink.GameFriendLink;

public class ScheduleFriendCommand extends Command {
    public static final String COMMAND_WORD = "--schedule";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Schedules free time for friend.\n"
            + "Parameters: FRIEND_ID --period START_HOUR END_HOUR DAY --free IS_FREE\n"
            + "Example: " + COMMAND_WORD + " Draco --period 0800 0900 3 --free 1\n";

    public static final String MESSAGE_SCHEDULE_FRIEND_SUCCESS = "Scheduled Friend: %1$s";
    private FriendId friendToScheduleId;
    private int day;
    private String startTime;
    private String endTime;
    private boolean isFree;

    /**
     * Command to schedule a friend using the unique FRIEND_ID.
     *
     * @param friendId  Id of friend to be scheduled.
     * @param day       Day to set.
     * @param startTime Start of timeslot.
     * @param endTime   End of timeslot.
     * @param isFree    Is timeslot free.
     */
    public ScheduleFriendCommand(FriendId friendId, int day, String startTime, String endTime, boolean isFree) {
        requireNonNull(friendId);
        friendToScheduleId = friendId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isFree = isFree;
    }

    /**
     * Creates friend with updated schedule.
     *
     * @param friendToSchedule Friend to update.
     * @return Friend with updated schedule.
     * @throws InvalidDayTimeException Invalid day or time format
     */
    private Friend createScheduledFriend(Friend friendToSchedule) throws InvalidDayTimeException {
        FriendId friendId = friendToSchedule.getFriendId();
        FriendName updatedFriendName = friendToSchedule.getFriendName();
        Map<GameId, GameFriendLink> gameFriendLinks = friendToSchedule.getGameFriendLinks();
        Schedule schedule = friendToSchedule.getSchedule();
        schedule.setScheduleDay(day, startTime, endTime, isFree);
        return new Friend(friendId, updatedFriendName, gameFriendLinks, schedule);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.hasFriendWithId(friendToScheduleId)) {
            throw new CommandException(Messages.MESSAGE_NONEXISTENT_FRIEND_ID);
        }

        try {
            Friend friendToSchedule = model.getFriend(friendToScheduleId);
            Friend scheduledFriend = createScheduledFriend(friendToSchedule);
            model.setFriend(friendToSchedule, scheduledFriend);
            return new CommandResult(String.format(MESSAGE_SCHEDULE_FRIEND_SUCCESS,
                    friendToScheduleId), CommandType.FRIEND_SCHEDULE);
        } catch (InvalidDayTimeException e) {
            throw new CommandException(String.format(MESSAGE_INVALID_DAY_TIME_FORMAT, e.getMessage()));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ScheduleFriendCommand // instanceof handles nulls
                && friendToScheduleId.equals(((ScheduleFriendCommand) other).friendToScheduleId)); // state check
    }
}
