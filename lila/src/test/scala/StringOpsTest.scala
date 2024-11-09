package scalalib

class StringTest extends munit.FunSuite:

  import scalalib.StringOps.*

  test("slug be safe >> html"):
    assert(!slug("hello \" world").contains("\""))
    assert(!slug("<<<").contains("<"))

  val i18nValidStrings = List(
    """éâòöÌÒÒçÇ""",
    """صارف اپنا نام تبدیل کریں۔ یہ صرف ایک دفعہ ہو سکتا ہے اور صرف انگریزی حروف چھوٹے یا بڑے کرنے کی اجازت ہے۔.""",
    """ユーザー名を変更します。これは一回限りで、行なえるのは大文字・小文字の変更だけです。""",
    """ਤੁਹਾਡੇ ਵਿਰੋਧੀ ਨੇ ਖੇਡ ਨੂੰ ਛੱਡ ਦਿੱਤਾ. ਤੁਸੀਂ ਜਿੱਤ ਦਾ ਦਾਅਵਾ ਕਰ ਸਕਦੇ ਹੋ, ਖੇਡ ਨੂੰ ਡਰਾਅ ਕਹਿ ਸਕਦੇ ਹੋ, ਜਾਂ ਇੰਤਜ਼ਾਰ ਕਰ ਸਕਦੇ ਹੋ.""",
    """మీ ప్రత్యర్థి బహుశా ఆట విడిచి వెళ్లిపోయారేమో. మీరు కాసేపు ఆగి చూడవచ్చు, లేదా గెలుపోటములు సమానంగా పంచుకోవచ్చు, లేదా విజయం ప్రకటించుకోవచ్చు.""",
    """ผู้เล่นที่เป็นคอมพิวเตอร์หรือใช้คอมพิวเตอร์ช่วย จะไม่ได้รับอนุญาตให้เล่น  โปรดอย่าใช้การช่วยเหลือจากตัวช่วยเล่นหมากรุก, ฐานข้อมูล หรือบุคคลอื่น ในขณะเล่น""",
    """သင့်ရဲ့ပြိုင်ဘက် ဂိမ်းမှထွက်ခွာသွားပါပြီ. လက်ရှိပွဲကို အနိုင်ယူမည်လား သရေကျပေးမည်လား သို့မဟုတ် စောင့်ဆိုင််းဦးမလား.""",
    """יריבך עזב את המשחק. באפשרותך לכפות פרישה, להכריז על תיקו או להמתין לו."""
  )

  val rms = removeMultibyteSymbols
  test("remove multibyte garbage"):
    assertEquals(rms("""🕸Trampas en Aperturas🕸: INTRO👋"""), "Trampas en Aperturas: INTRO")
    assertEquals(
      rms("""🚌🚎🚐🚑🚒🚓🚕🚗🚙🚚🚛🚜🚲🛴🛵🛺🦼🦽 with new and better !pizzes on lichess.org"""),
      " with new and better !pizzes on lichess.org"
    )
    assertEquals(rms("🥹"), "")
    assertEquals(rms("🥹🥹🥹 xxx 🥹"), " xxx ")
  test("preserve languages"):
    i18nValidStrings.foreach: txt =>
      assertEquals(rms(txt), txt)
  test("preserve half point"):
    assertEquals(rms("½"), "½")

  test("remove garbage chars"):
    assertEquals(removeGarbageChars("""ℱ۩۞۩꧁꧂"""), "")
    assertEquals(removeGarbageChars("""ᴀᴛᴏᴍɪᴄ"""), "")
    assertEquals(removeGarbageChars("""af éâòöÌÒÒçÇℱ۩۞۩꧁꧂"  صار"""), """af éâòöÌÒÒçÇ"  صار""")
    i18nValidStrings.foreach: txt =>
      assertEquals(removeGarbageChars(txt), txt)

  test("normalize keep º and ª"):
    assertEquals(normalize("keep normal text"), "keep normal text")
    assertEquals(normalize("keep º and ª"), "keep º and ª")
  test("normalize preserve half point"):
    assertEquals(normalize("½"), "½")

  test("invisible chars"):
    // normal space
    assertEquals(softCleanUp(" "), "")
    assertEquals(softCleanUp("    "), "")
    // braille space
    assertEquals(softCleanUp("⠀"), "")
    assertEquals(softCleanUp("⠀⠀⠀"), "")
    assertEquals(softCleanUp("⠀uh⠀⠀"), "uh")
