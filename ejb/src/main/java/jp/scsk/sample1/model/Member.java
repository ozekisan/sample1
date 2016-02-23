/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.scsk.sample1.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * ユーザエンティティ
 * <p>
 * 登録日時等の監査用項目はMappedSuperClass(AuditEntity)で管理する<br/>
 * getter/setterはlombok(@Data)で管理する<br/>
 * </p>
 *
 * @author Toshiyuki OZEKI
 *
 */
@SuppressWarnings({ "serial" })
@Entity
@XmlRootElement
@Table(name = "User_mst", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
public class Member implements Serializable {

	/**
	 * サロゲートキー
	 * <p>
	 * MySQLのAUTO_INCREMENTを使用せず、採番(NUMBERING)テーブルのNEXT_VAL列の値を格納する。<br/>
	 * （検索条件: SEQ_ID = 'MemberId'）<br/>
	 * NEXT_VAL列には、次回使用のためINCREMENT後の値が格納される。<br/>
	 * 連番をリセットするには、NUMBERINGテーブルのNEXT_VAL列の値をリセットする。
	 * </p>
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "member_seq")
	@TableGenerator(name = "member_seq", table = "NUMBERING", pkColumnName = "SEQ_ID", valueColumnName = "NEXT_VAL", pkColumnValue = "MemberId", allocationSize = 1)
	private Long id;

	@NotNull
	@Size(min = 1, max = 25)
	@Column(name = "user_name")
	@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
	private String name;

	/**
	 * バージョン。楽観的ロック時に使用する。
	 */
	@Version
	private Long version;

	/**
	 * 加盟店コード。数字のみで構成される。
	 */
	@Column(name = "membership_cd")
	@NotNull
	@Min(10000000)
	@Max(99999999)
	private Integer membershipCd;

	@NotNull
	@NotEmpty
	@Email
	private String email;

	@NotNull
	@Size(min = 10, max = 12)
	@Digits(fraction = 0, integer = 12)
	@Column(name = "phone_number")
	private String phoneNumber;
}
