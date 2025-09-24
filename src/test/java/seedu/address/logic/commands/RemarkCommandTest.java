@Test
public void execute() {
    final String remark = "Some remark";

    assertCommandFailure(new RemarkCommand(INDEX_FIRST_PERSON, remark), model,
        String.format(MESSAGE_ARGUMENTS, INDEX_FIRST_PERSON.getOneBased(), remark));
}