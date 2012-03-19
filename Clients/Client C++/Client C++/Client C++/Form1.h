#pragma once


namespace ClientC {

	using namespace System;
	using namespace System::ComponentModel;
	using namespace System::Collections;
	using namespace System::Windows::Forms;
	using namespace System::Data;
	using namespace System::Drawing;

	/// <summary>
	/// Description résumée de Form1
	///
	/// AVERTISSEMENT : si vous modifiez le nom de cette classe, vous devrez modifier la
	///          propriété 'Nom du fichier de ressources' de l'outil de compilation de ressource managée
	///          pour tous les fichiers .resx dont dépend cette classe. Dans le cas contraire,
	///          les concepteurs ne pourront pas interagir correctement avec les ressources
	///          localisées associées à ce formulaire.
	/// </summary>
	public ref class Form1 : public System::Windows::Forms::Form
	{
	public:
		Form1(void)
		{
			InitializeComponent();
			//
			//TODO : ajoutez ici le code du constructeur
			//
		}

	protected:
		/// <summary>
		/// Nettoyage des ressources utilisées.
		/// </summary>
		~Form1()
		{
			if (components)
			{
				delete components;
			}
		}
	private: System::Windows::Forms::PictureBox^  pictureBox1;
	private: System::Windows::Forms::Button^  BP_quitter;
	private: System::Windows::Forms::TabControl^  tab;

	private: System::Windows::Forms::TabPage^  tab_connexion;

	private: System::Windows::Forms::TabPage^  tab_compte;
	private: System::Windows::Forms::TextBox^  mdp;

	private: System::Windows::Forms::TextBox^  pseudo;

	private: System::Windows::Forms::TabPage^  tab_options;
	private: System::Windows::Forms::Button^  BP_connexion;
	private: System::Windows::Forms::Label^  label2;
	private: System::Windows::Forms::Label^  label1;
	private: System::Windows::Forms::Label^  label3;
	private: System::Windows::Forms::Button^  BP_creer_compte;
	private: System::Windows::Forms::TextBox^  textBox2;
	private: System::Windows::Forms::TextBox^  textBox1;
	private: System::Windows::Forms::Label^  label4;
	private: System::Windows::Forms::TabControl^  tabControl1;
	private: System::Windows::Forms::TabPage^  tab_changer_pseudo;
	private: System::Windows::Forms::TabPage^  tab_changer_mdp;
	private: System::Windows::Forms::Label^  label7;
	private: System::Windows::Forms::Label^  label6;
	private: System::Windows::Forms::Label^  label5;
	private: System::Windows::Forms::TextBox^  textBox5;
	private: System::Windows::Forms::TextBox^  textBox4;
	private: System::Windows::Forms::TextBox^  textBox3;
	private: System::Windows::Forms::Label^  label8;
	private: System::Windows::Forms::Label^  label9;
	private: System::Windows::Forms::Label^  label10;
	private: System::Windows::Forms::Label^  label11;
	private: System::Windows::Forms::Label^  label12;
	private: System::Windows::Forms::Label^  label13;
	private: System::Windows::Forms::Button^  BP_changer_pseudo;
	private: System::Windows::Forms::TextBox^  textBox8;
	private: System::Windows::Forms::TextBox^  textBox7;
	private: System::Windows::Forms::TextBox^  textBox6;
	private: System::Windows::Forms::Button^  BP_changer_mdp;






	protected: 


	private:
		/// <summary>
		/// Variable nécessaire au concepteur.
		/// </summary>
		System::ComponentModel::Container ^components;

#pragma region Windows Form Designer generated code
		/// <summary>
		/// Méthode requise pour la prise en charge du concepteur - ne modifiez pas
		/// le contenu de cette méthode avec l'éditeur de code.
		/// </summary>
		void InitializeComponent(void)
		{
			System::ComponentModel::ComponentResourceManager^  resources = (gcnew System::ComponentModel::ComponentResourceManager(Form1::typeid));
			this->pictureBox1 = (gcnew System::Windows::Forms::PictureBox());
			this->BP_quitter = (gcnew System::Windows::Forms::Button());
			this->tab = (gcnew System::Windows::Forms::TabControl());
			this->tab_connexion = (gcnew System::Windows::Forms::TabPage());
			this->BP_connexion = (gcnew System::Windows::Forms::Button());
			this->label2 = (gcnew System::Windows::Forms::Label());
			this->label1 = (gcnew System::Windows::Forms::Label());
			this->mdp = (gcnew System::Windows::Forms::TextBox());
			this->pseudo = (gcnew System::Windows::Forms::TextBox());
			this->tab_compte = (gcnew System::Windows::Forms::TabPage());
			this->textBox2 = (gcnew System::Windows::Forms::TextBox());
			this->textBox1 = (gcnew System::Windows::Forms::TextBox());
			this->label4 = (gcnew System::Windows::Forms::Label());
			this->label3 = (gcnew System::Windows::Forms::Label());
			this->BP_creer_compte = (gcnew System::Windows::Forms::Button());
			this->tab_options = (gcnew System::Windows::Forms::TabPage());
			this->tabControl1 = (gcnew System::Windows::Forms::TabControl());
			this->tab_changer_pseudo = (gcnew System::Windows::Forms::TabPage());
			this->tab_changer_mdp = (gcnew System::Windows::Forms::TabPage());
			this->label5 = (gcnew System::Windows::Forms::Label());
			this->label6 = (gcnew System::Windows::Forms::Label());
			this->label7 = (gcnew System::Windows::Forms::Label());
			this->label8 = (gcnew System::Windows::Forms::Label());
			this->label9 = (gcnew System::Windows::Forms::Label());
			this->label10 = (gcnew System::Windows::Forms::Label());
			this->label11 = (gcnew System::Windows::Forms::Label());
			this->label12 = (gcnew System::Windows::Forms::Label());
			this->label13 = (gcnew System::Windows::Forms::Label());
			this->textBox3 = (gcnew System::Windows::Forms::TextBox());
			this->textBox4 = (gcnew System::Windows::Forms::TextBox());
			this->textBox5 = (gcnew System::Windows::Forms::TextBox());
			this->textBox6 = (gcnew System::Windows::Forms::TextBox());
			this->textBox7 = (gcnew System::Windows::Forms::TextBox());
			this->textBox8 = (gcnew System::Windows::Forms::TextBox());
			this->BP_changer_mdp = (gcnew System::Windows::Forms::Button());
			this->BP_changer_pseudo = (gcnew System::Windows::Forms::Button());
			(cli::safe_cast<System::ComponentModel::ISupportInitialize^  >(this->pictureBox1))->BeginInit();
			this->tab->SuspendLayout();
			this->tab_connexion->SuspendLayout();
			this->tab_compte->SuspendLayout();
			this->tab_options->SuspendLayout();
			this->tabControl1->SuspendLayout();
			this->tab_changer_pseudo->SuspendLayout();
			this->tab_changer_mdp->SuspendLayout();
			this->SuspendLayout();
			// 
			// pictureBox1
			// 
			this->pictureBox1->Image = (cli::safe_cast<System::Drawing::Image^  >(resources->GetObject(L"pictureBox1.Image")));
			this->pictureBox1->Location = System::Drawing::Point(0, 0);
			this->pictureBox1->Name = L"pictureBox1";
			this->pictureBox1->Size = System::Drawing::Size(902, 550);
			this->pictureBox1->SizeMode = System::Windows::Forms::PictureBoxSizeMode::AutoSize;
			this->pictureBox1->TabIndex = 0;
			this->pictureBox1->TabStop = false;
			// 
			// BP_quitter
			// 
			this->BP_quitter->Location = System::Drawing::Point(13, 470);
			this->BP_quitter->Name = L"BP_quitter";
			this->BP_quitter->Size = System::Drawing::Size(120, 37);
			this->BP_quitter->TabIndex = 1;
			this->BP_quitter->Text = L"&Quitter";
			this->BP_quitter->UseVisualStyleBackColor = true;
			this->BP_quitter->Click += gcnew System::EventHandler(this, &Form1::button1_Click);
			// 
			// tab
			// 
			this->tab->Controls->Add(this->tab_connexion);
			this->tab->Controls->Add(this->tab_compte);
			this->tab->Controls->Add(this->tab_options);
			this->tab->Location = System::Drawing::Point(34, 104);
			this->tab->Name = L"tab";
			this->tab->SelectedIndex = 0;
			this->tab->Size = System::Drawing::Size(419, 334);
			this->tab->TabIndex = 3;
			// 
			// tab_connexion
			// 
			this->tab_connexion->Controls->Add(this->BP_connexion);
			this->tab_connexion->Controls->Add(this->label2);
			this->tab_connexion->Controls->Add(this->label1);
			this->tab_connexion->Controls->Add(this->mdp);
			this->tab_connexion->Controls->Add(this->pseudo);
			this->tab_connexion->Location = System::Drawing::Point(4, 22);
			this->tab_connexion->Name = L"tab_connexion";
			this->tab_connexion->Padding = System::Windows::Forms::Padding(3);
			this->tab_connexion->Size = System::Drawing::Size(411, 308);
			this->tab_connexion->TabIndex = 0;
			this->tab_connexion->Text = L"Connexion";
			this->tab_connexion->UseVisualStyleBackColor = true;
			// 
			// BP_connexion
			// 
			this->BP_connexion->Location = System::Drawing::Point(287, 243);
			this->BP_connexion->Name = L"BP_connexion";
			this->BP_connexion->Size = System::Drawing::Size(108, 32);
			this->BP_connexion->TabIndex = 4;
			this->BP_connexion->Text = L"&Connexion";
			this->BP_connexion->UseVisualStyleBackColor = true;
			// 
			// label2
			// 
			this->label2->AutoSize = true;
			this->label2->Location = System::Drawing::Point(25, 123);
			this->label2->Name = L"label2";
			this->label2->Size = System::Drawing::Size(71, 13);
			this->label2->TabIndex = 3;
			this->label2->Text = L"Mot de passe";
			// 
			// label1
			// 
			this->label1->AutoSize = true;
			this->label1->Location = System::Drawing::Point(25, 90);
			this->label1->Name = L"label1";
			this->label1->Size = System::Drawing::Size(43, 13);
			this->label1->TabIndex = 2;
			this->label1->Text = L"Pseudo";
			// 
			// mdp
			// 
			this->mdp->Location = System::Drawing::Point(110, 116);
			this->mdp->Name = L"mdp";
			this->mdp->Size = System::Drawing::Size(139, 20);
			this->mdp->TabIndex = 1;
			// 
			// pseudo
			// 
			this->pseudo->Location = System::Drawing::Point(110, 90);
			this->pseudo->Name = L"pseudo";
			this->pseudo->Size = System::Drawing::Size(139, 20);
			this->pseudo->TabIndex = 0;
			// 
			// tab_compte
			// 
			this->tab_compte->Controls->Add(this->textBox2);
			this->tab_compte->Controls->Add(this->textBox1);
			this->tab_compte->Controls->Add(this->label4);
			this->tab_compte->Controls->Add(this->label3);
			this->tab_compte->Controls->Add(this->BP_creer_compte);
			this->tab_compte->Location = System::Drawing::Point(4, 22);
			this->tab_compte->Name = L"tab_compte";
			this->tab_compte->Padding = System::Windows::Forms::Padding(3);
			this->tab_compte->Size = System::Drawing::Size(411, 308);
			this->tab_compte->TabIndex = 1;
			this->tab_compte->Text = L"Créer un compte";
			this->tab_compte->UseVisualStyleBackColor = true;
			// 
			// textBox2
			// 
			this->textBox2->Location = System::Drawing::Point(122, 116);
			this->textBox2->Name = L"textBox2";
			this->textBox2->Size = System::Drawing::Size(139, 20);
			this->textBox2->TabIndex = 6;
			// 
			// textBox1
			// 
			this->textBox1->Location = System::Drawing::Point(122, 88);
			this->textBox1->Name = L"textBox1";
			this->textBox1->Size = System::Drawing::Size(139, 20);
			this->textBox1->TabIndex = 5;
			// 
			// label4
			// 
			this->label4->AutoSize = true;
			this->label4->Location = System::Drawing::Point(19, 123);
			this->label4->Name = L"label4";
			this->label4->Size = System::Drawing::Size(71, 13);
			this->label4->TabIndex = 4;
			this->label4->Text = L"Mot de passe";
			// 
			// label3
			// 
			this->label3->AutoSize = true;
			this->label3->Location = System::Drawing::Point(19, 88);
			this->label3->Name = L"label3";
			this->label3->Size = System::Drawing::Size(43, 13);
			this->label3->TabIndex = 1;
			this->label3->Text = L"Pseudo";
			// 
			// BP_creer_compte
			// 
			this->BP_creer_compte->Location = System::Drawing::Point(284, 244);
			this->BP_creer_compte->Name = L"BP_creer_compte";
			this->BP_creer_compte->Size = System::Drawing::Size(103, 31);
			this->BP_creer_compte->TabIndex = 0;
			this->BP_creer_compte->Text = L"&Créer compte";
			this->BP_creer_compte->UseVisualStyleBackColor = true;
			// 
			// tab_options
			// 
			this->tab_options->Controls->Add(this->tabControl1);
			this->tab_options->Location = System::Drawing::Point(4, 22);
			this->tab_options->Name = L"tab_options";
			this->tab_options->Padding = System::Windows::Forms::Padding(3);
			this->tab_options->Size = System::Drawing::Size(411, 308);
			this->tab_options->TabIndex = 2;
			this->tab_options->Text = L"Options";
			this->tab_options->UseVisualStyleBackColor = true;
			// 
			// tabControl1
			// 
			this->tabControl1->Controls->Add(this->tab_changer_pseudo);
			this->tabControl1->Controls->Add(this->tab_changer_mdp);
			this->tabControl1->Location = System::Drawing::Point(42, 45);
			this->tabControl1->Name = L"tabControl1";
			this->tabControl1->SelectedIndex = 0;
			this->tabControl1->Size = System::Drawing::Size(311, 238);
			this->tabControl1->TabIndex = 0;
			// 
			// tab_changer_pseudo
			// 
			this->tab_changer_pseudo->Controls->Add(this->BP_changer_pseudo);
			this->tab_changer_pseudo->Controls->Add(this->textBox8);
			this->tab_changer_pseudo->Controls->Add(this->textBox7);
			this->tab_changer_pseudo->Controls->Add(this->textBox6);
			this->tab_changer_pseudo->Controls->Add(this->label7);
			this->tab_changer_pseudo->Controls->Add(this->label6);
			this->tab_changer_pseudo->Controls->Add(this->label5);
			this->tab_changer_pseudo->Location = System::Drawing::Point(4, 22);
			this->tab_changer_pseudo->Name = L"tab_changer_pseudo";
			this->tab_changer_pseudo->Padding = System::Windows::Forms::Padding(3);
			this->tab_changer_pseudo->Size = System::Drawing::Size(303, 212);
			this->tab_changer_pseudo->TabIndex = 0;
			this->tab_changer_pseudo->Text = L"Changer le Pseudo";
			this->tab_changer_pseudo->UseVisualStyleBackColor = true;
			// 
			// tab_changer_mdp
			// 
			this->tab_changer_mdp->Controls->Add(this->BP_changer_mdp);
			this->tab_changer_mdp->Controls->Add(this->textBox5);
			this->tab_changer_mdp->Controls->Add(this->textBox4);
			this->tab_changer_mdp->Controls->Add(this->textBox3);
			this->tab_changer_mdp->Controls->Add(this->label8);
			this->tab_changer_mdp->Controls->Add(this->label9);
			this->tab_changer_mdp->Controls->Add(this->label10);
			this->tab_changer_mdp->Location = System::Drawing::Point(4, 22);
			this->tab_changer_mdp->Name = L"tab_changer_mdp";
			this->tab_changer_mdp->Padding = System::Windows::Forms::Padding(3);
			this->tab_changer_mdp->Size = System::Drawing::Size(303, 212);
			this->tab_changer_mdp->TabIndex = 1;
			this->tab_changer_mdp->Text = L"Changer le mot de passe";
			this->tab_changer_mdp->UseVisualStyleBackColor = true;
			// 
			// label5
			// 
			this->label5->AutoSize = true;
			this->label5->Location = System::Drawing::Point(22, 51);
			this->label5->Name = L"label5";
			this->label5->Size = System::Drawing::Size(78, 13);
			this->label5->TabIndex = 0;
			this->label5->Text = L"Ancien pseudo";
			// 
			// label6
			// 
			this->label6->AutoSize = true;
			this->label6->Location = System::Drawing::Point(22, 82);
			this->label6->Name = L"label6";
			this->label6->Size = System::Drawing::Size(71, 13);
			this->label6->TabIndex = 1;
			this->label6->Text = L"Mot de passe";
			// 
			// label7
			// 
			this->label7->AutoSize = true;
			this->label7->Location = System::Drawing::Point(22, 116);
			this->label7->Name = L"label7";
			this->label7->Size = System::Drawing::Size(89, 13);
			this->label7->TabIndex = 2;
			this->label7->Text = L"Nouveau pseudo";
			// 
			// label8
			// 
			this->label8->AutoSize = true;
			this->label8->Location = System::Drawing::Point(31, 108);
			this->label8->Name = L"label8";
			this->label8->Size = System::Drawing::Size(117, 13);
			this->label8->TabIndex = 5;
			this->label8->Text = L"Nouveau mot de passe";
			// 
			// label9
			// 
			this->label9->AutoSize = true;
			this->label9->Location = System::Drawing::Point(31, 74);
			this->label9->Name = L"label9";
			this->label9->Size = System::Drawing::Size(106, 13);
			this->label9->TabIndex = 4;
			this->label9->Text = L"Ancien mot de passe";
			// 
			// label10
			// 
			this->label10->AutoSize = true;
			this->label10->Location = System::Drawing::Point(31, 43);
			this->label10->Name = L"label10";
			this->label10->Size = System::Drawing::Size(43, 13);
			this->label10->TabIndex = 3;
			this->label10->Text = L"Pseudo";
			// 
			// label11
			// 
			this->label11->AutoSize = true;
			this->label11->Location = System::Drawing::Point(24, 98);
			this->label11->Name = L"label11";
			this->label11->Size = System::Drawing::Size(43, 13);
			this->label11->TabIndex = 2;
			this->label11->Text = L"Pseudo";
			// 
			// label12
			// 
			this->label12->AutoSize = true;
			this->label12->Location = System::Drawing::Point(24, 64);
			this->label12->Name = L"label12";
			this->label12->Size = System::Drawing::Size(71, 13);
			this->label12->TabIndex = 1;
			this->label12->Text = L"Mot de passe";
			// 
			// label13
			// 
			this->label13->AutoSize = true;
			this->label13->Location = System::Drawing::Point(24, 33);
			this->label13->Name = L"label13";
			this->label13->Size = System::Drawing::Size(79, 13);
			this->label13->TabIndex = 0;
			this->label13->Text = L"Ancien Pseudo";
			// 
			// textBox3
			// 
			this->textBox3->Location = System::Drawing::Point(158, 37);
			this->textBox3->Name = L"textBox3";
			this->textBox3->Size = System::Drawing::Size(122, 20);
			this->textBox3->TabIndex = 6;
			// 
			// textBox4
			// 
			this->textBox4->Location = System::Drawing::Point(158, 74);
			this->textBox4->Name = L"textBox4";
			this->textBox4->Size = System::Drawing::Size(122, 20);
			this->textBox4->TabIndex = 7;
			// 
			// textBox5
			// 
			this->textBox5->Location = System::Drawing::Point(158, 108);
			this->textBox5->Name = L"textBox5";
			this->textBox5->Size = System::Drawing::Size(122, 20);
			this->textBox5->TabIndex = 8;
			// 
			// textBox6
			// 
			this->textBox6->Location = System::Drawing::Point(161, 51);
			this->textBox6->Name = L"textBox6";
			this->textBox6->Size = System::Drawing::Size(118, 20);
			this->textBox6->TabIndex = 3;
			// 
			// textBox7
			// 
			this->textBox7->Location = System::Drawing::Point(161, 82);
			this->textBox7->Name = L"textBox7";
			this->textBox7->Size = System::Drawing::Size(118, 20);
			this->textBox7->TabIndex = 4;
			// 
			// textBox8
			// 
			this->textBox8->Location = System::Drawing::Point(161, 116);
			this->textBox8->Name = L"textBox8";
			this->textBox8->Size = System::Drawing::Size(118, 20);
			this->textBox8->TabIndex = 5;
			// 
			// BP_changer_mdp
			// 
			this->BP_changer_mdp->Location = System::Drawing::Point(219, 174);
			this->BP_changer_mdp->Name = L"BP_changer_mdp";
			this->BP_changer_mdp->Size = System::Drawing::Size(60, 23);
			this->BP_changer_mdp->TabIndex = 9;
			this->BP_changer_mdp->Text = L"&Ok";
			this->BP_changer_mdp->UseVisualStyleBackColor = true;
			// 
			// BP_changer_pseudo
			// 
			this->BP_changer_pseudo->Location = System::Drawing::Point(223, 169);
			this->BP_changer_pseudo->Name = L"BP_changer_pseudo";
			this->BP_changer_pseudo->Size = System::Drawing::Size(55, 27);
			this->BP_changer_pseudo->TabIndex = 6;
			this->BP_changer_pseudo->Text = L"&Ok";
			this->BP_changer_pseudo->UseVisualStyleBackColor = true;
			// 
			// Form1
			// 
			this->AutoScaleDimensions = System::Drawing::SizeF(6, 13);
			this->AutoScaleMode = System::Windows::Forms::AutoScaleMode::Font;
			this->ClientSize = System::Drawing::Size(899, 552);
			this->Controls->Add(this->tab);
			this->Controls->Add(this->BP_quitter);
			this->Controls->Add(this->pictureBox1);
			this->Name = L"Form1";
			this->Text = L"Accueil";
			(cli::safe_cast<System::ComponentModel::ISupportInitialize^  >(this->pictureBox1))->EndInit();
			this->tab->ResumeLayout(false);
			this->tab_connexion->ResumeLayout(false);
			this->tab_connexion->PerformLayout();
			this->tab_compte->ResumeLayout(false);
			this->tab_compte->PerformLayout();
			this->tab_options->ResumeLayout(false);
			this->tabControl1->ResumeLayout(false);
			this->tab_changer_pseudo->ResumeLayout(false);
			this->tab_changer_pseudo->PerformLayout();
			this->tab_changer_mdp->ResumeLayout(false);
			this->tab_changer_mdp->PerformLayout();
			this->ResumeLayout(false);
			this->PerformLayout();

		}
#pragma endregion
	private: System::Void button1_Click(System::Object^  sender, System::EventArgs^  e) {
				 Application::Exit();
			 }
	};
}

