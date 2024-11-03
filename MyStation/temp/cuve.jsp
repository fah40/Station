<%@page import="model.Cuve"%>
<%@page import="model.PrelevementCuve"%>
<%
  Cuve[] all= (Cuve[])request.getAttribute("listCuve");
  PrelevementCuve[] allP= (PrelevementCuve[])request.getAttribute("listPlCuve");
%>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta content="width=device-width, initial-scale=1.0" name="viewport">

  <title>cuve</title>
  <meta content="" name="description">
  <meta content="" name="keywords">

  <!-- Favicons -->
  <link href="assets/img/favicon.png" rel="icon">
  <link href="assets/img/apple-touch-icon.png" rel="apple-touch-icon">

  

  <!-- Vendor CSS Files -->
  <link href="assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <link href="assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
  <link href="assets/vendor/boxicons/css/boxicons.min.css" rel="stylesheet">
  <link href="assets/vendor/quill/quill.snow.css" rel="stylesheet">
  <link href="assets/vendor/quill/quill.bubble.css" rel="stylesheet">
  <link href="assets/vendor/remixicon/remixicon.css" rel="stylesheet">
  <link href="assets/vendor/simple-datatables/style.css" rel="stylesheet">

  <!-- Template Main CSS File -->
  <link href="assets/css/style.css" rel="stylesheet">

</head>

<body>

  <!-- ======= Header ======= -->

  <!-- ======= Sidebar ======= -->
   <aside id="sidebar" class="sidebar">
    <br>

    <ul class="sidebar-nav" id="sidebar-nav">

      <li class="nav-item">
        <a class="nav-link " href="home.jsp">
          <i class="bi bi-grid"></i>
          <span>Acceuil</span>
        </a>
      </li><!-- End Dashboard Nav -->

      <li class="nav-item">
        <a class="nav-link collapsed" data-bs-target="#Prelevement-nav" data-bs-toggle="collapse" href="#">
          <i class="bi bi-menu-button-wide"></i><span>Prelevement</span><i class="bi bi-chevron-down ms-auto"></i>
        </a>
        <ul id="Prelevement-nav" class="nav-content collapse " data-bs-parent="#sidebar-nav">
          <li>
            <a href="PrelevementCuveServlet">
              <i class="bi bi-circle"></i><span>cuve</span>
            </a>
          </li>
          <li>
            <a href="PrelevementCompteurServlet">
              <i class="bi bi-circle"></i><span>compteur</span>
            </a>
          </li>
        </ul>
      </li><!-- End Prelevement Nav -->

      <li class="nav-item">
        <a class="nav-link collapsed" data-bs-target="#Encaissement-nav" data-bs-toggle="collapse" href="#">
          <i class="bi bi-menu-button-wide"></i><span>Comptablilite</span><i class="bi bi-chevron-down ms-auto"></i>
        </a>
        <ul id="Encaissement-nav" class="nav-content collapse " data-bs-parent="#sidebar-nav">
          <li>
            <a href="save">
              <i class="bi bi-circle"></i><span>achat</span>
            </a>
          </li>
          <li>
            <a href="Save">
              <i class="bi bi-circle"></i><span>encaissement</span>
            </a>
          </li>
        </ul>
      </li><!-- End Encaissement Nav -->
    </ul>

  </aside><!-- End Sidebar-->

  <main id="main" class="main">

    <div class="col-md-2-offset-10" style="text-align: end">

      <div class="btn-group" role="group" aria-label="Basic example">
        <button type="button" class="btn">
           <i class="bi bi-box-arrow-in-right"></i>
          <span>Deconnection</span>
        </button>
        <button type="button" class="btn">
          <i class="bi bi-list toggle-sidebar-btn"></i> 
        </button>
      </div>

      <!-- <div class="col-md-1">
        <a class="nav-link collapsed" href="pages-login.html">
          <i class="bi bi-box-arrow-in-right"></i>
          <span>Deconnection</span>
        </a> 
      </div>
      <div class="col-md-1">
        <i class="bi bi-list toggle-sidebar-btn"></i> 
      </div> -->
       
    </div>
    <div class="pagetitle" >
      <h1>Gestion Station</h1>
    </div><!-- End Page Title -->

    <section class="section dashboard">
      <div class="row">

        <!-- Left side columns -->
        <div class="col-lg-8">
          <div class="row">

            <!-- Top Selling -->
            <div class="col-12">

              <div class="card">
                <div class="card-body">
                  <h5 class="card-title" > Liste Prelevement</h5>

                  <!-- Default Table -->
                  <table class="table">
                    <thead>
                      <tr>
                        <th scope="col">#</th>
                        <th scope="col">Pompiste</th>
                        <th scope="col">Cuve</th>
                        <th scope="col">mesure</th>
                        <th scope="col">quantite</th>
                        <th scope="col">compteur</th>
                        <th scope="col">Date</th>
                      </tr>
                    </thead>
                    <tbody>
                      <%for (int i=0; i<allP.length; i++) { %>
                        <tr>
                          <th scope="row"><%=allP[i].getId()%></th>
                          <td><%=allP[i].getPompiste().getNom()%></td>
                          <td>cuve-<%=allP[i].getCuve().getId()%></td>
                          <td><%=allP[i].getValeurCuve()%></td>
                          <%String color="black"; if(allP[i].getQuantite() != allP[i].getCompteur()) {color="red";}%>
                          <td style="color: <%=color%>"><%=allP[i].getQuantite()%></td>
                          <td><%=allP[i].getCompteur()%></td>
                          <td><%=allP[i].getDate()%></td>
                        </tr>
                      <%} %>
                    </tbody>
                  </table>
                  <!-- End Default Table Example -->
                </div>
              </div>
            </div><!-- End Top Selling -->

          </div>
        </div><!-- End Left side columns -->

        <!-- Right side columns -->
        <div class="col-lg-4">
           <div class="card">
                <div class="card-body">
                  <h5 class="card-title">Vente</h5>
                  <!-- Multi Columns Form -->
                  <form class="row g-3" action="PrelevementCuveServlet" method="post">
                    <div class="col-md-12">
                      <label for="inputState" class="form-label">Cuve</label>
                      <select id="inputState" class="form-select" name="idCuve">
                        <option selected>Choose...</option>
                        <%for(int x=0;x< all.length;x++) {%>
                        <option value="<%=all[x].getId()%>">Cuve-<%=all[x].getId()%></option>
                        <%}%>
                      </select>
                    </div>

                    <div class="col-md-">
                      <label for="inputPassword5" class="form-label">mesure(s) Cuve</label>
                      <input type="" class="form-control" id="inputPassword5" name="valeur">
                    </div>

                    <div class="col-12">
                      <label for="inputAddress5" class="form-label">date</label>
                      <input type="date" class="form-control" id="inputAddres5s" name="date">
                    </div>

                    <div class="text-center">
                      <button type="submit" class="btn btn-primary">Submit</button>
                      <button type="reset" class="btn btn-secondary">Reset</button>
                    </div>
                  </form><!-- End Multi Columns Form -->

                </div>
              </div>
        </div><!-- End Right side columns -->

      </div>
    </section>

  </main><!-- End #main -->

  <!-- ======= Footer ======= -->
  <footer id="footer" class="footer">
    <div class="copyright">
      &copy; Copyright <strong><span>JoA</span></strong>.
    </div>
    <div class="credits">
      ETU 2363
    </div>
  </footer><!-- End Footer -->

  <a href="#" class="back-to-top d-flex align-items-center justify-content-center"><i class="bi bi-arrow-up-short"></i></a>

  <!-- Vendor JS Files -->
  <script src="assets/vendor/apexcharts/apexcharts.min.js"></script>
  <script src="assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="assets/vendor/chart.js/chart.umd.js"></script>
  <script src="assets/vendor/echarts/echarts.min.js"></script>
  <script src="assets/vendor/quill/quill.min.js"></script>
  <script src="assets/vendor/simple-datatables/simple-datatables.js"></script>
  <script src="assets/vendor/tinymce/tinymce.min.js"></script>
  <script src="assets/vendor/php-email-form/validate.js"></script>

  <!-- Template Main JS File -->
  <script src="assets/js/main.js"></script>

</body>

</html>